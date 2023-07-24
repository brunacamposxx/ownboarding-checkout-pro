package checkoutpro.dto;

import lombok.Data;

@Data
public class Payer {
  private String id;
  private String email;
  private String firstName;
  private String lastName;
  private Identification identification;
}
