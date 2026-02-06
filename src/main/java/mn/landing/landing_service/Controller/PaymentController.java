package mn.landing.landing_service.Controller;

import mn.landing.landing_service.Entity.Payment;
import mn.landing.landing_service.Entity.User;
import mn.landing.landing_service.Model.*;
import mn.landing.landing_service.Repository.PaymentRepository;
import mn.landing.landing_service.Service.CurrentUserService;
import mn.landing.landing_service.Service.PaymentService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    private final PaymentService paymentService;
    private final PaymentRepository paymentRepository;
    private final CurrentUserService currentUserService;

    public PaymentController(PaymentService paymentService,
                             PaymentRepository paymentRepository,
                             CurrentUserService currentUserService) {
        this.paymentService = paymentService;
        this.paymentRepository = paymentRepository;
        this.currentUserService = currentUserService;
    }

    @PostMapping("/checkout")
    public PaymentCheckoutResponse checkout(@RequestBody PaymentCheckoutRequest req) {
        User me = currentUserService.requireUser();
        Payment p = paymentService.checkout(me, req.planCode);
        return new PaymentCheckoutResponse(p.getId(), p.getInvoiceId(), p.getStatus());
    }

    @GetMapping("/me")
    public List<PaymentSummaryResponse> myPayments() {
        User me = currentUserService.requireUser();

        // ✅ ТАНАЙХ одоогоор PaymentRepository дээр subscription->user шүүлт байгаа тул үүнийг ашиглав.
        return paymentRepository
                .findBySubscription_User_IdAndDeletedFalseOrderByCreatedAtDesc(me.getId())
                .stream()
                .map(p -> new PaymentSummaryResponse(
                        p.getId(),
                        p.getSubscription() == null ? null : p.getSubscription().getId(),
                        p.getProvider(),
                        p.getStatus(),
                        p.getAmount(),
                        p.getCurrency(),
                        p.getInvoiceId(),
                        p.getPaidAt(),
                        p.getCreatedAt()
                ))
                .toList();
    }
}
