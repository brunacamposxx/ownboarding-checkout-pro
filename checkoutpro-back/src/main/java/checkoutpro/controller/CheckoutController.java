package checkoutpro.controller;

import checkoutpro.dto.CreditCardResponse;
import checkoutpro.dto.PaymentRequestDTO;
import checkoutpro.service.CheckoutService;
import checkoutpro.dto.PreferenceRequestDTO;
import com.mercadopago.net.MPResourceList;
import com.mercadopago.resources.payment.Payment;
import com.mercadopago.resources.paymentmethod.PaymentMethod;
import com.mercadopago.resources.preference.Preference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "*")
@RestController
public class CheckoutController {
  @Autowired
  private CheckoutService service;
  @RequestMapping("/create_preference")
  @PostMapping
  public Preference createPreference(@RequestBody PreferenceRequestDTO preference) {
    return service.preference(preference);
  }

  @GetMapping("/payment_methods")
  public MPResourceList<PaymentMethod> getPaymentMethods() {
    return service.getPaymentMethods();
  }

  @PostMapping("/process_payment")
  public CreditCardResponse sendPayment(@RequestBody PaymentRequestDTO payment) {
    System.out.println(payment);
    return service.sendPayment(payment);

  }

  @PostMapping("/pix")
  public Payment sendPix(@RequestBody PaymentRequestDTO payment) {
    System.out.println(payment);
    return service.sendPix(payment);

  }

  @PostMapping("/boleto")
  public Payment createBoleto(@RequestBody PaymentRequestDTO payment) {
    return service.createBoleto(payment);
  }

  @PostMapping("/preference_for_brick")
  public Preference createPreferenceForBrick() {
    return service.getPreference();
  }

//  @PostMapping("/add_new_card")
//    public CustomerCard addNewCard(@RequestBody PaymentRequestDTO payment) {
//        return service.addNewCard(payment);
//    }

//@GetMapping("/get_cards")
//    public MPResourceList<CustomerCard> getCards() {
//        return service.getCards();
//    }


}
