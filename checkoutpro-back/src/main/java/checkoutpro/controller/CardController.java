package checkoutpro.controller;


import checkoutpro.dto.CreditCardResponse;
import checkoutpro.dto.PaymentRequestDTO;
import checkoutpro.service.CardService;
import com.mercadopago.net.MPResourceList;
import com.mercadopago.net.MPResultsResourcesPage;
import com.mercadopago.resources.customer.Customer;
import com.mercadopago.resources.customer.CustomerCard;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "*")
@RestController
public class CardController {
  @Autowired
  private CardService service;

  @PostMapping("/save_card")
  public CustomerCard saveCard(@RequestBody PaymentRequestDTO payment) {
    return service.saveCreditCard(payment);
  }

  @PutMapping("/update_card/{id}")
  public Customer updateCustomer(@RequestBody PaymentRequestDTO payment, @PathVariable String id) {
    return service.updateCustomer(payment, id);
  }

  @GetMapping("/customer/search")
  public MPResultsResourcesPage<Customer> searchCustomer(@RequestParam String email) {
    return service.searchCustomer(email);
  }

  @PostMapping("/customer/add_card")
  public CustomerCard addCard(@RequestBody PaymentRequestDTO payment) {
    return service.addCard(payment);
  }

  @GetMapping("/customer/{id}/cards")
  public MPResourceList<CustomerCard> getCardsList(@PathVariable String id) {
    System.out.println(id);
    return service.getCards(id);
  }

  @PostMapping("/customer/create_payment")
    public CreditCardResponse createPayment(@RequestBody String token) {
        return service.createPayment(token);
    }
}
