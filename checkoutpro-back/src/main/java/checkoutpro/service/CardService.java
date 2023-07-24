package checkoutpro.service;


import checkoutpro.dto.CreditCardResponse;
import checkoutpro.dto.PaymentRequestDTO;
import com.mercadopago.MercadoPagoConfig;
import com.mercadopago.client.common.IdentificationRequest;
import com.mercadopago.client.customer.CustomerCardClient;
import com.mercadopago.client.customer.CustomerCardCreateRequest;
import com.mercadopago.client.customer.CustomerClient;
import com.mercadopago.client.customer.CustomerRequest;
import com.mercadopago.client.payment.PaymentClient;
import com.mercadopago.client.payment.PaymentCreateRequest;
import com.mercadopago.client.payment.PaymentPayerRequest;
import com.mercadopago.exceptions.MPApiException;
import com.mercadopago.exceptions.MPException;
import com.mercadopago.net.MPResourceList;
import com.mercadopago.net.MPResultsResourcesPage;
import com.mercadopago.net.MPSearchRequest;
import com.mercadopago.resources.customer.Customer;
import com.mercadopago.resources.customer.CustomerCard;
import com.mercadopago.resources.customer.CustomerCardIssuer;
import com.mercadopago.resources.payment.Payment;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class CardService {
  @Value("${PROD_ACCESS_TOKEN}")
  private String acessToken;

  public CustomerCard saveCreditCard(PaymentRequestDTO payment) {
    try {
      MercadoPagoConfig.setAccessToken(acessToken);

      CustomerClient customerClient = new CustomerClient();
      CustomerCardClient customerCardClient = new CustomerCardClient();

      CustomerRequest customerRequest = CustomerRequest.builder()
          .email(payment.getPayer().getEmail())
          .build();
      Customer customer = customerClient.create(customerRequest);

      CustomerCardIssuer issuer = CustomerCardIssuer.builder()
          .id(payment.getIssuerId())
          .build();

      CustomerCardCreateRequest cardCreateRequest = CustomerCardCreateRequest.builder()
          .token(payment.getToken())
          .issuer(issuer)
          .paymentMethodId(payment.getPaymentMethodId())
          .build();

      return customerCardClient.create(customer.getId(), cardCreateRequest);

    } catch (MPApiException ex) {
      System.out.printf(
          "MercadoPago Error. Status: %s, Content: %s%n",
          ex.getApiResponse().getStatusCode(), ex.getApiResponse().getContent());
    } catch (MPException e) {
      e.printStackTrace();
    }
    return null;
  }

  public Customer updateCustomer(PaymentRequestDTO payment, String customerId) {
    try {
      MercadoPagoConfig.setAccessToken(acessToken);

      CustomerClient client = new CustomerClient();

      CustomerRequest request = CustomerRequest.builder()
          .firstName(payment.getPayer().getFirstName())
          .lastName(payment.getPayer().getLastName())
          .identification(IdentificationRequest.builder()
              .type(payment.getPayer().getIdentification().getType())
              .number(payment.getPayer().getIdentification().getNumber())
              .build())
          .description(payment.getDescription())
          .build();

      return client.update(customerId, request);
    } catch (MPApiException ex) {
      System.out.printf(
          "MercadoPago Error. Status: %s, Content: %s%n",
          ex.getApiResponse().getStatusCode(), ex.getApiResponse().getContent());
    } catch (MPException e) {
      e.printStackTrace();
    }
    return null;
  }

  public MPResultsResourcesPage<Customer> searchCustomer(String email) {
    MercadoPagoConfig.setAccessToken(acessToken);

    CustomerClient client = new CustomerClient();

    Map<String, Object> filters = new HashMap<>();
    filters.put("email", email);

    MPSearchRequest searchRequest =
        MPSearchRequest.builder().offset(0).limit(0).filters(filters).build();

    try {
      return client.search(searchRequest);
  } catch (MPApiException ex) {
    System.out.printf(
        "MercadoPago Error. Status: %s, Content: %s%n",
        ex.getApiResponse().getStatusCode(), ex.getApiResponse().getContent());
  } catch (MPException e) {
    e.printStackTrace();
  }
    return null;
  }

  public CustomerCard addCard(PaymentRequestDTO payment) {
    MercadoPagoConfig.setAccessToken(acessToken);
    try {
      MPResultsResourcesPage<Customer> client = searchCustomer(payment.getPayer().getEmail());
      String customerId = client.getResults().get(0).getId();

      CustomerClient customerClient = new CustomerClient();
      CustomerCardClient customerCardClient = new CustomerCardClient();

      Customer customer = customerClient.get(customerId);

      CustomerCardIssuer issuer = CustomerCardIssuer.builder()
          .id(payment.getIssuerId())
          .build();

      CustomerCardCreateRequest cardCreateRequest = CustomerCardCreateRequest.builder()
          .token(payment.getToken())
          .issuer(issuer)
          .paymentMethodId(payment.getPaymentMethodId())
          .build();

      CustomerCard card = customerCardClient.create(customer.getId(), cardCreateRequest);
      return card;
    } catch (MPApiException ex) {
      System.out.printf(
          "MercadoPago Error. Status: %s, Content: %s%n",
          ex.getApiResponse().getStatusCode(), ex.getApiResponse().getContent());
    } catch (MPException e) {
      e.printStackTrace();
    }
    return null;
  }

    public MPResourceList<CustomerCard> getCards(String customerId) {
        MercadoPagoConfig.setAccessToken(acessToken);
        try {
          CustomerClient customerClient = new CustomerClient();

          Customer customer = customerClient.get(customerId);
          return customerClient.listCards(customer.getId());

        } catch (MPApiException ex) {
        System.out.printf(
            "MercadoPago Error. Status: %s, Content: %s%n",
            ex.getApiResponse().getStatusCode(), ex.getApiResponse().getContent());
        } catch (MPException e) {
        e.printStackTrace();
        }
        return null;
    }

    public CreditCardResponse createPayment(String token) {
      MercadoPagoConfig.setAccessToken(acessToken);
      try {
      PaymentClient client = new PaymentClient();
      System.out.println(token);
        PaymentCreateRequest paymentCreateRequest =
            PaymentCreateRequest.builder()
                .transactionAmount(new BigDecimal("100.5"))
                .token(token)
                .description("teste testinho")
                .installments(1)
                .paymentMethodId("master")
                .issuerId("24")
                .payer(PaymentPayerRequest.builder()
                      .type("customer")
                      .id("1270398066-HOCZwfub5HG2Te")
                    .build())
                .build();

        Payment request = client.create(paymentCreateRequest);

        return CreditCardResponse.builder()
            .id(request.getId())
            .status(request.getStatus())
            .statusDetail(request.getStatusDetail())
            .paymentTypeId(request.getPaymentTypeId())
            .paymentMethodId(request.getPaymentMethodId())
            .dateApproved(request.getDateApproved())
            .transactionAmount(request.getTransactionAmount())
            .payer(PaymentPayerRequest.builder()
                .email(request.getPayer().getEmail())
                .build())
            .build();
      } catch (MPApiException ex) {
          System.out.printf(
              "MercadoPago Error. Status: %s, Content: %s%n",
              ex.getApiResponse().getStatusCode(), ex.getApiResponse().getContent());
        } catch (MPException e) {
          e.printStackTrace();
        }
      return null;

        }
}
