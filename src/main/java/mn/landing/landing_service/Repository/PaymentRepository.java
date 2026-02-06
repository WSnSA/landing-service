package mn.landing.landing_service.Repository;

import mn.landing.landing_service.Entity.Payment;
import mn.landing.landing_service.Entity.PaymentStatus;
import mn.landing.landing_service.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {

    // ✅ "My payments" (Payment -> Subscription -> User)
    List<Payment> findBySubscription_User_IdAndDeletedFalseOrderByCreatedAtDesc(Long userId);

    // (optional convenience)
    List<Payment> findBySubscription_UserAndDeletedFalseOrderByCreatedAtDesc(User user);

    // ✅ Admin payments by status
    List<Payment> findByStatusAndDeletedFalseOrderByCreatedAtDesc(PaymentStatus status);

    Optional<Payment> findByInvoiceIdAndDeletedFalse(String invoiceId);
}
