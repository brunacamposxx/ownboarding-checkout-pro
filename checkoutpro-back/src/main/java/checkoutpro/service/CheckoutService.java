package checkoutpro.service;

import checkoutpro.dto.CreditCardResponse;
import checkoutpro.dto.PaymentRequestDTO;
import checkoutpro.dto.PreferenceRequestDTO;
import com.mercadopago.MercadoPagoConfig;
import com.mercadopago.client.common.IdentificationRequest;
import com.mercadopago.client.payment.PaymentClient;
import com.mercadopago.client.payment.PaymentCreateRequest;
import com.mercadopago.client.payment.PaymentPayerRequest;
import com.mercadopago.client.paymentmethod.PaymentMethodClient;
import com.mercadopago.client.preference.PreferenceClient;
import com.mercadopago.client.preference.PreferenceItemRequest;
import com.mercadopago.client.preference.PreferenceRequest;
import com.mercadopago.exceptions.MPApiException;
import com.mercadopago.exceptions.MPException;
import com.mercadopago.net.MPResourceList;
import com.mercadopago.resources.payment.Payment;
import com.mercadopago.resources.paymentmethod.PaymentMethod;
import com.mercadopago.resources.preference.Preference;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class CheckoutService {
  String prodAccessToken = System.getenv("PROD_ACCESS_TOKEN");
  String testAccessToken = System.getenv("TEST_ACCESS_TOKEN");
  String testRealUserAT = System.getenv("TEST_AT_REAL_USER");
  
  public Preference preference(PreferenceRequestDTO preference) {

    MercadoPagoConfig.setAccessToken(testRealUserAT);

    PreferenceItemRequest itemRequest =
        PreferenceItemRequest.builder()
            .id(preference.getId())
            .title(preference.getTitle())
            .description(preference.getDescription())
            .pictureUrl(preference.getPictureUrl())
            .categoryId(preference.getCategoryId())
            .quantity(preference.getQuantity())
            .currencyId("BRL")
            .unitPrice(preference.getUnitPrice())
            .build();

    List<PreferenceItemRequest> items = new ArrayList<>();
    items.add(itemRequest);

    PreferenceRequest preferenceRequest = PreferenceRequest.builder()
        .items(items).build();

    PreferenceClient client = new PreferenceClient();

    try {
      var cliente = client.create(preferenceRequest);
      System.out.println(cliente.getInitPoint());
      return cliente;
    } catch (MPApiException ex) {
      System.out.printf(
          "MercadoPago Error. Status: %s, Content: %s%n",
          ex.getApiResponse().getStatusCode(), ex.getApiResponse().getContent());
    } catch (MPException e) {
      e.printStackTrace();
    }
    return null;
  }

  public MPResourceList<PaymentMethod> getPaymentMethods() {
    MercadoPagoConfig.setAccessToken(prodAccessToken);
    try {
      PaymentMethodClient client = new PaymentMethodClient();
      System.out.println(client.list());
      return client.list();
    } catch (MPException | MPApiException e) {
      e.printStackTrace();
      throw new RuntimeException(e);
    }
  }

  public CreditCardResponse sendPayment(PaymentRequestDTO payment) {
    try {
      MercadoPagoConfig.setAccessToken(prodAccessToken);
      PaymentClient client = new PaymentClient();

      PaymentCreateRequest paymentCreateRequest =
          PaymentCreateRequest.builder()
              .transactionAmount(payment.getTransactionAmount())
              .token(payment.getToken())
              .description(payment.getDescription())
              .installments(payment.getInstallments())
              .paymentMethodId(payment.getPaymentMethodId())
              .issuerId(payment.getIssuerId())
              .payer(PaymentPayerRequest.builder()
                  .id(payment.getPayer().getId())
                  .email(payment.getPayer().getEmail())
                  .identification(
                      IdentificationRequest.builder()
                          .type(payment.getPayer().getIdentification().getType())
                          .number(payment.getPayer().getIdentification().getNumber())
                          .build())
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
    } catch (MPException ex) {
      ex.printStackTrace();
    }
    return null;
  }

  public Payment sendPix(PaymentRequestDTO payment) {
    try {
      MercadoPagoConfig.setAccessToken(prodAccessToken);
      PaymentClient client = new PaymentClient();

      PaymentCreateRequest paymentCreateRequest =
          PaymentCreateRequest.builder()
              .transactionAmount(payment.getTransactionAmount())
              .token(payment.getToken())
              .description(payment.getDescription())
              .installments(payment.getInstallments())
              .paymentMethodId(payment.getPaymentMethodId())
              .issuerId(payment.getIssuerId())
              .payer(PaymentPayerRequest.builder()
                  .id(payment.getPayer().getId())
                  .email(payment.getPayer().getEmail())
                  .identification(
                      IdentificationRequest.builder()
                          .type(payment.getPayer().getIdentification().getType())
                          .number(payment.getPayer().getIdentification().getNumber())
                          .build())
                  .build())
              .build();

      return client.create(paymentCreateRequest);

    } catch (MPApiException ex) {
      System.out.printf(
          "MercadoPago Error. Status: %s, Content: %s%n",
          ex.getApiResponse().getStatusCode(), ex.getApiResponse().getContent());
    } catch (MPException ex) {
      ex.printStackTrace();
    }
    return null;
  }

  public Payment createBoleto(PaymentRequestDTO payment) {
    MercadoPagoConfig.setAccessToken(prodAccessToken);

    PaymentClient client = new PaymentClient();

    PaymentCreateRequest paymentCreateRequest =
        PaymentCreateRequest.builder()
            .transactionAmount(payment.getTransactionAmount())
            .description(payment.getDescription())
            .paymentMethodId(payment.getPaymentMethodId())
            .dateOfExpiration(OffsetDateTime.of(2023, 8, 1, 10, 10, 10, 0, ZoneOffset.UTC))
            .payer(
                PaymentPayerRequest.builder()
                    .email(payment.getPayer().getEmail())
                    .firstName(payment.getPayer().getFirstName())
                    .lastName(payment.getPayer().getLastName())
                    .identification(
                        IdentificationRequest.builder().type(payment.getPayer().getIdentification().getType())
                            .number(payment.getPayer().getIdentification().getNumber()).build())
                    .build())
            .build();
    try {
      var cliente = client.create(paymentCreateRequest);
      System.out.println(cliente.getTransactionDetails().getExternalResourceUrl());
      return cliente;
    } catch (MPApiException ex) {
      System.out.printf(
          "MercadoPago Error. Status: %s, Content: %s%n",
          ex.getApiResponse().getStatusCode(), ex.getApiResponse().getContent());
    } catch (MPException e) {
      e.printStackTrace();
    }
    return null;
  }

  public Preference getPreference() {
    MercadoPagoConfig.setAccessToken(prodAccessToken);

    PreferenceClient client = new PreferenceClient();

    List<PreferenceItemRequest> items = new ArrayList<>();
    PreferenceItemRequest item =
        PreferenceItemRequest.builder()
            .title("Meu produto")
            .quantity(1)
            .unitPrice(new BigDecimal("100"))
            .build();
    items.add(item);

    PreferenceRequest request = PreferenceRequest.builder()
        // o .purpose('wallet_purchase') permite apenas pagamentos logados
        // para permitir pagamentos como guest, você pode omitir essa linha
//        .purpose("wallet_purchase")
        .items(items).build();

    try {
      System.out.println(client.create(request));
      return client.create(request);
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