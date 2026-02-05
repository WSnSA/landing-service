package mn.landing.landing_service.Model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import mn.landing.landing_service.Entity.PaymentProvider;
import mn.landing.landing_service.Entity.PaymentStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
public class PaymentSummaryResponse {
    public Long id;
    public Long subscriptionId;
    public PaymentProvider provider;
    public PaymentStatus status;
    public BigDecimal amount;
    public String currency;
    public String invoiceId;
    public LocalDateTime paidAt;
    public LocalDateTime createdAt;

    public PaymentSummaryResponse(Long id, Long subscriptionId, PaymentProvider provider, PaymentStatus status,
                                  BigDecimal amount, String currency, String invoiceId,
                                  LocalDateTime paidAt, LocalDateTime createdAt) {
        this.id = id;
        this.subscriptionId = subscriptionId;
        this.provider = provider;
        this.status = status;
        this.amount = amount;
        this.currency = currency;
        this.invoiceId = invoiceId;
        this.paidAt = paidAt;
        this.createdAt = createdAt;
    }
}
