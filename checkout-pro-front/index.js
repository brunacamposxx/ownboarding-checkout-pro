let preference;
  const orderData = {
          quantity: 1,
          unitPrice: 10
        };
      
fetch("http://localhost:8080/create_preference", {
  method: "POST",
  headers: {
    "Content-Type": "application/json",
  },
  body: JSON.stringify(orderData),
})
  .then((response) => response.json())
  .then((data) => {
    preference = data.id;
    console.log("aqui", preference);

    mp.bricks().create("wallet", "wallet_container", {
      initialization: {
        preferenceId: preference,
      },
    });
  });