package mn.landing.landing_service.Controller;

import mn.landing.landing_service.Entity.PaymentStatus;
import mn.landing.landing_service.Entity.User;
import mn.landing.landing_service.Model.PaymentSummaryResponse;
import mn.landing.landing_service.Repository.PaymentRepository;
import mn.landing.landing_service.Service.CurrentUserService;
import mn.landing.landing_service.Service.PaymentService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/payments")
public class AdminPaymentController {

    private final PaymentService paymentService;
    private final PaymentRepository paymentRepository;
    private final CurrentUserService currentUserService;

    public AdminPaymentController(PaymentService paymentService,
                                  PaymentRepository paymentRepository,
                                  CurrentUserService currentUserService) {
        this.paymentService = paymentService;
        this.paymentRepository = paymentRepository;
        this.currentUserService = currentUserService;
    }

    @PreAuthorize("hasRole('ADMIN') or hasAuthority('ADMIN') or hasAuthority('ROLE_ADMIN')")
    @PostMapping("/{paymentId}/mark-paid")
    public void markPaid(@PathVariable Long paymentId) {
        User admin = currentUserService.requireAdmin();
        paymentService.markPaidManual(admin, paymentId);
    }

    @PreAuthorize("hasRole('ADMIN') or hasAuthority('ADMIN') or hasAuthority('ROLE_ADMIN')")
    @GetMapping("/pending")
    public List<PaymentSummaryResponse> pending() {
        currentUserService.requireAdmin();
        return paymentRepository.findByStatusAndDeletedFalseOrderByCreatedAtDesc(PaymentStatus.PENDING)
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
