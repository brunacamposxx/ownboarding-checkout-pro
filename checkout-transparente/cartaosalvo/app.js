const securityCodeElement = mp.fields.create('securityCode', {
  placeholder: "CVV"
}).mount('form-checkout__securityCode-container');

async function fetchCustomerCards() {
  try {
    const response = await fetch("http://localhost:8080/customer/1270398066-HOCZwfub5HG2Te/cards");

    const data = await response.json();
    const customerCards = data.results;
    return customerCards;
  } catch (error) {
    console.error('Error fetching customer cards:', error);
    return [];
  }
}

const appendCardToSelect = async () => {
  const selectElement = document.getElementById('form-checkout__cardId');
  const customerCards = await fetchCustomerCards();
  const tmpFragment = document.createDocumentFragment();

  customerCards.forEach(({ id, lastFourDigits, paymentMethod }) => {
    const optionElement = document.createElement('option');
    optionElement.setAttribute('value', id);

    const paymentMethodName = paymentMethod.name;
    optionElement.textContent = `${paymentMethodName} ended in ${lastFourDigits}`;
    tmpFragment.appendChild(optionElement);
  });

  selectElement.appendChild(tmpFragment);
};

appendCardToSelect();


const sendPaymentData = async (token) => {
  try {
    const response = await fetch('http://www.localhost:8080/customer/create_payment', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: token
    });

    if (!response.ok) {
      throw new Error('Failed to process payment');
    }

    const data = await response.json();
    console.log('Payment processed successfully:', data);

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

  } catch (error) {
    console.error('Error processing payment:', error);
  }
};

const formElement = document.getElementById('form-checkout');
formElement.addEventListener('submit', e => createCardToken(e));

const createCardToken = async (event) => {
 
  try {
    const tokenElement = document.getElementById('token');
    if (!tokenElement.value) {
      event.preventDefault();
      const token = await mp.fields.createCardToken({
        cardId: document.getElementById('form-checkout__cardId').value,
      });
      tokenElement.value = token.id;
      console.log(tokenElement.value);

      sendPaymentData(token.id);
    }
  } catch (e) {
    console.error('Error creating card token: ', e);
  }
};