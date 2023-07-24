
(async function getIdentificationTypes() {
    try {
      const identificationTypes = await mp.getIdentificationTypes();
      const identificationTypeElement = document.getElementById('form-checkout__identificationType');

      createSelectOptions(identificationTypeElement, identificationTypes);
    } catch (e) {
      return console.error('Error getting identificationTypes: ', e);
    }
  })();

  function createSelectOptions(elem, options, labelsAndKeys = { label: "name", value: "id" }) {
    const { label, value } = labelsAndKeys;

    elem.options.length = 0;

    const tempOptions = document.createDocumentFragment();

    options.forEach(option => {
      const optValue = option[value];
      const optLabel = option[label];

      const opt = document.createElement('option');
      opt.value = optValue;
      opt.textContent = optLabel;

      tempOptions.appendChild(opt);
    });

    elem.appendChild(tempOptions);
  }

async function sendPayment() {
  const firstName = document.getElementById('form-checkout__payerFirstName');
  const lastName = document.getElementById('form-checkout__payerLastName');
  const email = document.getElementById('form-checkout__email');
  const identificationType = document.getElementById('form-checkout__identificationType');
  const identificationNumber = document.getElementById('form-checkout__identificationNumber');
  const transactionAmount = document.getElementById('transactionAmount');
  const description = document.getElementById('description');
  const paymentMethodId = document.getElementById('paymentMethodId');
  const dateOfExpiration = document.getElementById('dateOfExpiration');

  const data = {
      transaction_amount: transactionAmount.value,
      description: description.value,
      payment_method_id: paymentMethodId.value,
      date_of_expiration: dateOfExpiration.value,
      payer: {
        email: email.value,
        firstName: firstName.value,
        lastName: lastName.value,
        identification: {
          type: identificationType.value,
          number: identificationNumber.value
        }
      }
  }

  const api = await fetch("http://localhost:8080/boleto", {
    method: 'POST',
    mode: "cors",
    headers: {
      'Content-Type': 'application/json',
    },
    body: JSON.stringify(data)
  })
  console.log(data);

  const response = await api.json();
  console.log(response);

  const boleto = response.transactionDetails.externalResourceUrl;
  
  const link = document.createElement('a');
  link.href = boleto;
  link.target = "_blank";
  link.textContent = "Pagar com Boleto";
  link.style.display = 'block'; 

  const container2 = document.getElementById('container');
  container2.appendChild(link);

}