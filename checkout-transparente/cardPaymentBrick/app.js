

const bricksBuilder = mp.bricks();

const renderCardPaymentBrick = async (bricksBuilder) => {
    const settings = {
      initialization: {
        amount: 100, // valor total a ser pago
      },
      callbacks: {
        onReady: () => {
          /*
            Callback chamado quando o Brick estiver pronto.
            Aqui você pode ocultar loadings do seu site, por exemplo.
          */
        },
        onSubmit: (formData) => {
            console.log("formData", formData)
          // callback chamado ao clicar no botão de submissão dos dados
          return new Promise((resolve, reject) => {
            fetch('http://localhost:8080/process_payment', {
              method: 'POST',
              headers: {
                'Content-Type': 'application/json',
              },
              body: JSON.stringify(formData),
            })
              .then((data) => data.json())
              .then((data) => {console.log("backend response", data)
              
                // receber o resultado do pagamento
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
        resultContainer.innerHTML = creditCardInfo;
  })
                resolve();
              })
              .catch((error) => {
                // lidar com a resposta de erro ao tentar criar o pagamento
                reject();
              });
        },
        onError: (error) => {
          // callback chamado para todos os casos de erro do Brick
          console.error(error);
        },
      },
     };
     window.cardPaymentBrickController = await bricksBuilder.create(
      'cardPayment',
      'cardPaymentBrick_container',
      settings,
     );  
   };
   renderCardPaymentBrick(bricksBuilder);
   