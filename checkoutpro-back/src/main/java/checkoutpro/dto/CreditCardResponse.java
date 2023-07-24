package checkoutpro.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mercadopago.client.payment.PaymentPayerRequest;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CreditCardResponse {
  private Long id;
  private String status;
  @JsonProperty("status_detail")
  private String statusDetail;
  @JsonProperty("payment_type_id")
  private String paymentTypeId;
  @JsonProperty("payment_method_id")
  private String paymentMethodId;
  @JsonProperty("date_approved")
  private OffsetDateTime dateApproved;
  private PaymentPayerRequest payer;
  @JsonProperty("transaction_amount")
  private BigDecimal transactionAmount;
}
