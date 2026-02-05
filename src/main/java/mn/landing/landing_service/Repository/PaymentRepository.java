package mn.landing.landing_service.Repository;

import mn.landing.landing_service.Entity.Payment;
import mn.landing.landing_service.Entity.PaymentStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment, Long> {

    Optional<Payment> findByInvoiceIdAndDeletedFalse(String invoiceId);


    List<Payment> findByStatusAndDeletedFalseOrderByCreatedAtDesc(PaymentStatus status);

    Optional<Object> findBySubscription_User_IdAndDeletedFalseOrderByCreatedAtDesc(Long id);

}
