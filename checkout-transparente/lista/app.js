fetch('http://localhost:8080/payment_methods')
  .then(response => response.json())
  .then(data => {
    const table = document.createElement('table');
    const thead = document.createElement('thead');
    const tbody = document.createElement('tbody');

    // Cria o cabeçalho da tabela
    const trHead = document.createElement('tr');
    const thId = document.createElement('th');
    thId.textContent = 'ID';
    const thName = document.createElement('th');
    thName.textContent = 'Nome';
    const thPaymentTypeId = document.createElement('th');
    trHead.appendChild(thId);
    trHead.appendChild(thName);
    trHead.appendChild(thPaymentTypeId);
    thead.appendChild(trHead);

    // Cria as linhas da tabela com base nos dados recebidos
    data.results.forEach(paymentMethod => {
      const trBody = document.createElement('tr');
      const tdId = document.createElement('td');
      tdId.textContent = paymentMethod.id;
      const tdName = document.createElement('td');
      tdName.textContent = paymentMethod.name;
      const tdPaymentTypeId = document.createElement('td');
      tdPaymentTypeId.textContent = paymentMethod.payment_type_id;
			const tdImage = document.createElement('td');
			const img = document.createElement('img');
			img.src = paymentMethod.thumbnail;
			tdImage.appendChild(img);
			trBody.appendChild(tdId);
			trBody.appendChild(tdName);
			trBody.appendChild(tdPaymentTypeId);
			trBody.appendChild(tdImage);
      tbody.appendChild(trBody);
    });

    // Adiciona a tabela à página
    table.appendChild(thead);
    table.appendChild(tbody);
    document.body.appendChild(table);
  });
