package mn.landing.landing_service.Model;

import mn.landing.landing_service.Entity.PaymentStatus;

public class PaymentCheckoutResponse {
    public Long paymentId;
    public String invoiceId;
    public PaymentStatus status;

    public PaymentCheckoutResponse(Long paymentId, String invoiceId, PaymentStatus status) {
        this.paymentId = paymentId;
        this.invoiceId = invoiceId;
        this.status = status;
    }
}
