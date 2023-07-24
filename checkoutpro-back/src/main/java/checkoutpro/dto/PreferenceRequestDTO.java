package checkoutpro.dto;

import java.math.BigDecimal;
import lombok.Data;

@Data
public class PreferenceRequestDTO {
  private String id;
  private String title;
  private String description;
  private String pictureUrl;
  private String categoryId;
  private Integer quantity;
  private BigDecimal unitPrice;

}
