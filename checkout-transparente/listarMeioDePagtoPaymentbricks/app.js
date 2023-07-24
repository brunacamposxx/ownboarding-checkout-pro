const bricksBuilder = mp.bricks();

const renderPaymentBrick = async (bricksBuilder) => {
    const settings = {
      initialization: {

        amount: 100,
        preferenceId: "",
      },
      customization: {
        paymentMethods: {
          ticket: "all",
          bankTransfer: "all",
          creditCard: "all",
          debitCard: "all",
          mercadoPago: "all",
        },
      },
      callbacks: {
        onReady: () => {
          /*
           Callback chamado quando o Brick estiver pronto.
           Aqui você pode ocultar loadings do seu site, por exemplo.
          */
        },
        onSubmit: ({ selectedPaymentMethod, formData }) => {
          console.log("formData", formData)
          console.log("selectedPaymentMethod", selectedPaymentMethod)

          // callback chamado ao clicar no botão de submissão dos dados
          return new Promise((resolve, reject) => {
            fetch("http://localhost:8080/preference_for_brick", {
              method: "POST",
              headers: {
                "Content-Type": "application/json",
              },
              body: JSON.stringify(formData),
            })
              .then((response) => response.json())
              .then((response) => {
                console.log(response);
                settings.initialization.preferenceId = response.id;
                // receber o resultado do pagamento
                resolve();
              })
              .catch((error) => {
                // lidar com a resposta de erro ao tentar criar o pagamento
                reject();
              });
          });
        },
        onError: (error) => {
          // callback chamado para todos os casos de erro do Brick
          console.error(error);
        },
      },
    };
    window.paymentBrickController = await bricksBuilder.create(
      "payment",
      "paymentBrick_container",
      settings
    );
   };
   renderPaymentBrick(bricksBuilder);
   