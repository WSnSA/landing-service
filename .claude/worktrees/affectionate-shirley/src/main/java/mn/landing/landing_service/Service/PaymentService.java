package mn.landing.landing_service.Service;

import mn.landing.landing_service.Entity.*;
import mn.landing.landing_service.Repository.PaymentRepository;
import mn.landing.landing_service.Repository.SubscriptionPlanRepository;
import mn.landing.landing_service.Repository.SubscriptionRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final SubscriptionRepository subscriptionRepository;
    private final SubscriptionPlanRepository planRepository;

    @Value("${app.payment.provider:MOCK}")
    private String provider;

    @Value("${app.payment.manual-enabled:false}")
    private boolean manualEnabled;

    public PaymentService(PaymentRepository paymentRepository,
                          SubscriptionRepository subscriptionRepository,
                          SubscriptionPlanRepository planRepository) {
        this.paymentRepository = paymentRepository;
        this.subscriptionRepository = subscriptionRepository;
        this.planRepository = planRepository;
    }

    /**
     * Checkout:
     *  - Subscription (PENDING байхыг зөвлөж байна; байхгүй бол ACTIVE fallback)
     *  - Payment (PENDING)
     */
    @Transactional
    public Payment checkout(User user, String planCode) {
        if (user == null) throw new RuntimeException("UNAUTHORIZED");
        if (planCode == null || planCode.isBlank()) throw new RuntimeException("PLAN_CODE_REQUIRED");

        SubscriptionPlan plan = planRepository.findByCode(planCode.trim().toUpperCase())
                .orElseThrow(() -> new RuntimeException("PLAN_NOT_FOUND"));

        LocalDateTime now = LocalDateTime.now();
        int durationDays = resolveDurationDays(plan);

        // 1) Subscription үүсгэнэ
        Subscription sub = new Subscription();
        sub.setUser(user);
        sub.setPlan(plan);
        sub.setStatus(resolveSubscriptionStatus("PENDING", SubscriptionStatus.ACTIVE));
        sub.setStartAt(now);
        sub.setEndAt(now.plusDays(durationDays));
        sub.setRenewalAt(sub.getEndAt());

        sub = subscriptionRepository.save(sub);

        // 2) Payment үүсгэнэ
        Payment p = new Payment();
        p.setSubscription(sub);
        p.setProvider(resolvePaymentProvider());
        p.setStatus(PaymentStatus.PENDING);
        p.setAmount(resolvePlanAmount(plan));
        p.setCurrency("MNT");
        p.setInvoiceId("MOCK-" + UUID.randomUUID());
        return paymentRepository.save(p);
    }

    /**
     * ✅ ADMIN + manualEnabled=true үед л ажиллана.
     * Payment=PAID, Subscription=ACTIVE (хугацааг одооноос эхлүүлж reset)
     */
    @Transactional
    public void markPaidManual(User adminUser, Long paymentId) {
        if (!manualEnabled) throw new RuntimeException("PAYMENT_MANUAL_DISABLED");
        if (adminUser == null) throw new RuntimeException("UNAUTHORIZED");
        if (adminUser.getRole() == null || !adminUser.getRole().toString().equalsIgnoreCase("ADMIN")) {
            throw new RuntimeException("FORBIDDEN");
        }

        Payment p = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new RuntimeException("PAYMENT_NOT_FOUND"));

        if (p.getStatus() == PaymentStatus.PAID) return; // idempotent

        LocalDateTime now = LocalDateTime.now();

        p.setStatus(PaymentStatus.PAID);
        p.setPaidAt(now);
        paymentRepository.save(p);

        Subscription sub = p.getSubscription();
        if (sub == null) throw new RuntimeException("SUBSCRIPTION_MISSING");

        // өмнөх ACTIVE subscription байвал хугацааг нь дуусгаж өгнө (enum шаардахгүйгээр)
        subscriptionRepository.findFirstByUserAndDeletedFalseAndStatusAndEndAtAfterOrderByEndAtDesc(
                sub.getUser(), SubscriptionStatus.ACTIVE, now
        ).ifPresent(old -> {
            if (!old.getId().equals(sub.getId())) {
                old.setEndAt(now);        // active check автоматаар унана
                old.setRenewalAt(null);
                subscriptionRepository.save(old);
            }
        });

        int durationDays = resolveDurationDays(sub.getPlan());
        sub.setStatus(resolveSubscriptionStatus("ACTIVE", sub.getStatus()));
        sub.setStartAt(now);
        sub.setEndAt(now.plusDays(durationDays));
        sub.setRenewalAt(sub.getEndAt());

        subscriptionRepository.save(sub);
    }

    // ---------------- helpers ----------------

    private PaymentProvider resolvePaymentProvider() {
        try {
            return PaymentProvider.valueOf(provider.trim().toUpperCase());
        } catch (Exception e) {
            PaymentProvider[] vals = PaymentProvider.values();
            if (vals.length == 0) throw new RuntimeException("PAYMENT_PROVIDER_EMPTY");
            return vals[0];
        }
    }

    private SubscriptionStatus resolveSubscriptionStatus(String desired, SubscriptionStatus fallback) {
        try {
            return SubscriptionStatus.valueOf(desired);
        } catch (Exception e) {
            return fallback; // enum дээр PENDING байхгүй бол ACTIVE fallback
        }
    }

    private BigDecimal resolvePlanAmount(SubscriptionPlan plan) {
        BigDecimal v = tryCallBigDecimal(plan, "getPrice");
        if (v != null) return v;
        v = tryCallBigDecimal(plan, "getAmount");
        if (v != null) return v;
        v = tryCallBigDecimal(plan, "getMonthlyPrice");
        if (v != null) return v;
        return BigDecimal.ZERO; // mock default
    }

    private int resolveDurationDays(SubscriptionPlan plan) {
        Integer v = tryCallInteger(plan, "getDurationDays");
        if (v != null && v > 0) return v;
        v = tryCallInteger(plan, "getPeriodDays");
        if (v != null && v > 0) return v;
        v = tryCallInteger(plan, "getDays");
        if (v != null && v > 0) return v;
        return 30;
    }

    private BigDecimal tryCallBigDecimal(Object target, String methodName) {
        try {
            Method m = target.getClass().getMethod(methodName);
            Object r = m.invoke(target);
            if (r == null) return null;
            if (r instanceof BigDecimal bd) return bd;
            if (r instanceof Number n) return BigDecimal.valueOf(n.doubleValue());
            return null;
        } catch (Exception ignored) {
            return null;
        }
    }

    private Integer tryCallInteger(Object target, String methodName) {
        try {
            Method m = target.getClass().getMethod(methodName);
            Object r = m.invoke(target);
            if (r == null) return null;
            if (r instanceof Integer i) return i;
            if (r instanceof Number n) return n.intValue();
            return null;
        } catch (Exception ignored) {
            return null;
        }
    }
}
