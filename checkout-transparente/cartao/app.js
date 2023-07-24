
const cardForm = mp.cardForm({
  amount: "100.5",
  iframe: true,
  form: {
    id: "form-checkout",
    cardNumber: {
      id: "form-checkout__cardNumber",
      placeholder: "Número do cartão",
    },
    expirationDate: {
      id: "form-checkout__expirationDate",
      placeholder: "MM/YY",
    },
    securityCode: {
      id: "form-checkout__securityCode",
      placeholder: "Código de segurança",
    },
    cardholderName: {
      id: "form-checkout__cardholderName",
      placeholder: "Titular do cartão",
    },
    issuer: {
      id: "form-checkout__issuer",
      placeholder: "Banco emissor",
    },
    installments: {
      id: "form-checkout__installments",
      placeholder: "Parcelas",
    },        
    identificationType: {
      id: "form-checkout__identificationType",
      placeholder: "Tipo de documento",
    },
    identificationNumber: {
      id: "form-checkout__identificationNumber",
      placeholder: "Número do documento",
    },
    cardholderEmail: {
      id: "form-checkout__cardholderEmail",
      placeholder: "E-mail",
    },
  },
  callbacks: {
    onFormMounted: error => {
      if (error) return console.warn("Form Mounted handling error: ", error);
      console.log("Form mounted");
    },
    onSubmit: event => {
      event.preventDefault();

      const {
        paymentMethodId: payment_method_id,
        issuerId: issuer_id,
        cardholderEmail: email,
        amount,
        token,
        installments,
        identificationNumber,
        identificationType,
      } = cardForm.getCardFormData();
      console.log(cardForm.getCardFormData())

      fetch("http://localhost:8080/process_payment", 
      {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify({
          token,
          issuer_id,
          payment_method_id,
          transaction_amount: Number(amount),
          installments: Number(installments),
          description: "Descrição do produto",
          payer: {
            email,
            identification: {
              type: identificationType,
              number: identificationNumber,
            },
          },
        }),
      }).then(response => response.json())
      .then(data => {
        console.log(data);

        const resultContainer = document.getElementById("result-container");
        const creditCardInfo = `
          <p><strong>Id:</strong> ${data.id}</p>
          <p class="status-${data.status.toLowerCase()}"><strong>Status:</strong> ${data.status}</p>
          <p><strong>Status Detail:</strong> ${data.status_detail}</p>
          <p><strong>Payment Type Id:</strong> ${data.payment_type_id}</p>
          <p><strong>Payment Method Id:</strong> ${data.payment_method_id}</p>
          <p><strong>Date Approved:</strong> ${data.date_approved}</p>
          <p><strong>Payer:</strong> ${data.payer.email}</p>
          <p><strong>Transaction Amount:</strong> ${data.transaction_amount}</p>
        `;
    resultContainer.hidden = false;
        resultContainer.innerHTML = creditCardInfo;
  })
},
  onFetching: (resource) => {
    console.log("Fetching resource: ", resource);

    // Animate progress bar
    const progressBar = document.querySelector(".progress-bar");
    progressBar.removeAttribute("value");

    return () => {
      progressBar.setAttribute("value", "0");
    };
  }
},
});