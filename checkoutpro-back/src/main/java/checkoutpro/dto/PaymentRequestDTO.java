package checkoutpro.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import lombok.Data;

@Data
public class PaymentRequestDTO {
  private String token;
  @JsonProperty("issuer_id")
  private String issuerId;
  @JsonProperty("transaction_amount")
  private BigDecimal transactionAmount;
  private String description;
  private Integer installments;
  @JsonProperty("payment_method_id")
  private String paymentMethodId;
  @JsonProperty("date_of_expiration")
  private OffsetDateTime dateOfExpiration;
  private Payer payer;
}

