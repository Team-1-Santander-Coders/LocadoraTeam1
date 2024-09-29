(function(){
    emailjs.init("-Z51VkHqRSKon-Gi5");
})();

document.addEventListener("DOMContentLoaded", function () {
    loadCustomers();
    loadAvailableVehicles()
    loadAgencies();
    loadRentals();

    document.getElementById('rentalForm').addEventListener('submit', function (event) {
        event.preventDefault();

        const customerData = document.getElementById('customer').value.split(" / ");
        const vehicleData = document.getElementById('vehicle').value.split(" / ");
        const pickupAgencyData = document.getElementById('pickupAgency').value.split(" / ");
        const startDate = formatDate(document.getElementById('startDate').value);
        let endDate = 'Sem data de devolução';

        const customerDocument = customerData[2];
        const vehiclePlaca = vehicleData[2];
        const pickupAgencyName = pickupAgencyData[0];
        const pickupAgencyAddress = pickupAgencyData[1];
        const dropoffAgencyName = 'Sem agência de devolução';
        const dropoffAgencyAddress = 'Sem agência de devolução';

        fetchRent(vehiclePlaca, pickupAgencyName, pickupAgencyAddress, startDate, dropoffAgencyName, dropoffAgencyAddress, endDate, customerDocument);
    });
});



function fetchRent(vehiclePlaca, pickupAgencyName, pickupAgencyAddress, startDate, dropoffAgencyName, dropoffAgencyAddress, endDate, customerDocument) {
    fetch('/rent', {
        method: 'POST',
        body: `${vehiclePlaca} / ${pickupAgencyName} / ${pickupAgencyAddress} / ${startDate} / ${dropoffAgencyName} / ${dropoffAgencyAddress} / ${endDate} / ${customerDocument}`,
        credentials: 'include',
    }).then(response => {
        if (response.ok) {
            alert('Aluguel criado com sucesso!');
            document.getElementById('rentalForm').reset();
            loadAvailableVehicles();
            loadRentals();
        } else {
            alert('Erro ao criar aluguel.');
        }
    }).catch(error => {
        console.error('Erro ao fazer requisição:', error);
        alert('Falha na comunicação com o servidor.');
    });
}

function loadAvailableVehicles() {
    fetch('/vehicles')
        .then(response => response.text())
        .then(data => {
            const availableVehicles = data.split('\n').filter(vehicle => vehicle.includes('Disponível'));
            const vehicleSelect = document.getElementById('vehicle');
            vehicleSelect.innerHTML = '';

            availableVehicles.forEach(vehicle => {
                const [placa, modelo, tipo] = vehicle.split(' - ');
                const option = document.createElement('option');
                option.textContent = `${placa} / ${modelo} / ${tipo}`;
                option.value = `${placa} / ${modelo} / ${tipo}`;
                vehicleSelect.appendChild(option);
            });
        })
        .catch(error => {
            console.error('Erro ao carregar veículos disponíveis:', error);
            alert('Erro ao carregar veículos disponíveis.');
        });
}

function formatDate(dateString) {
    const dateParts = dateString.split('-');
    const year = dateParts[0];
    const month = dateParts[1];
    const day = dateParts[2];

    return `${day}/${month}/${year}`;
}

function loadCustomers() {
    fetch('/users')
        .then(response => {
            if (!response.ok) {
                throw new Error('Erro na resposta do servidor');
            }
            return response.json();
        })
        .then(customers => {
            const customerSelect = document.getElementById('customer');
            customerSelect.innerHTML = '';

            customers.forEach(customer => {
                const customerDocument = customer.document;
                const customerName = customer.name;
                const customerEmail = customer.email;

                const option = document.createElement('option');
                option.textContent = `${capitalizeFirstLetters(customerName)} - ${customerEmail}`;
                option.value = `${customerName} / ${customerEmail} / ${customerDocument}`;
                customerSelect.appendChild(option);
            });
        })
        .catch(error => {
            console.error('Erro ao carregar clientes:', error);
            alert('Erro ao carregar clientes.');
        });
}


function loadAgencies() {
    fetch('/agencies')
        .then(response => response.json())
        .then(agencies => {
            const pickupAgencySelect = document.getElementById('pickupAgency');
            const dropoffAgencySelect = document.getElementById('agencyReturn');
            pickupAgencySelect.innerHTML = '';
            dropoffAgencySelect.innerHTML = '';
            const option = document.createElement('option');

            option.textContent = "";
            option.value = "";
            dropoffAgencySelect.appendChild(option);

            agencies.forEach(agency => {
                const option1 = document.createElement('option');
                option1.textContent = `${agency.name} / ${agency.address}`;
                option1.value = `${agency.name} / ${agency.address}`;
                pickupAgencySelect.appendChild(option1);

                const option2 = document.createElement('option');
                option2.textContent = `${agency.name} / ${agency.address}`;
                option2.value = `${agency.name} / ${agency.address}`;
                dropoffAgencySelect.appendChild(option2);
            });
        })
        .catch(error => {
            console.error('Erro ao carregar agências:', error);
            alert('Erro ao carregar agências.');
        });
}


function loadRentals() {
    fetch('/rentals')
        .then(response => {
            if (!response.ok) {
                throw new Error(`Erro na resposta: ${response.statusText}`);
            }
            return response.text();
        })
        .then(text => {
            try {
                const cleanedText = text.trim();
                const rentals = JSON.parse(cleanedText);

                if (Array.isArray(rentals)) {
                    populateRentalTable(rentals);
                } else {
                    populateRentalTable([rentals]);
                }
            } catch (error) {
                console.error('Erro ao analisar JSON:', error);
                alert('Erro ao analisar os dados recebidos.');
            }
        })
        .catch(error => {
            console.error('Erro ao carregar aluguéis:', error);
            alert('Erro ao carregar aluguéis.');
        });
}

function populateRentalTable(rentals) {
    const rentalTableBody = document.querySelector('#rentalTable tbody');
    rentalTableBody.innerHTML = '';

    rentals.forEach(rental => {
        const row = document.createElement('tr');

        const agencyCell = document.createElement('td');
        agencyCell.textContent = rental.agenciaRetirada;
        row.appendChild(agencyCell);

        const customerCell = document.createElement('td');
        customerCell.textContent = rental.cliente;
        row.appendChild(customerCell);

        const vehicleCell = document.createElement('td');
        vehicleCell.textContent = rental.veiculo;
        row.appendChild(vehicleCell);

        const rentalDateCell = document.createElement('td');
        rentalDateCell.textContent = rental.dataRetirada;
        row.appendChild(rentalDateCell);

        const statusCell = document.createElement('td');
        statusCell.textContent = rental.situacao;
        row.appendChild(statusCell);

        if (rental.situacao === "Devolvido.") {
            const receiptButtonCell = document.createElement('td');
            const receiptButton = document.createElement('button');
            receiptButton.textContent = 'Gerar Recibo';
            receiptButton.addEventListener('click', () => generateReceipt(rental));
            receiptButtonCell.appendChild(receiptButton);
            const emailButton = document.createElement('button');
            emailButton.textContent = 'Enviar E-mail';
            emailButton.style.marginLeft = '10px';
            emailButton.addEventListener('click', () => sendRentalReceipt(rental));
            receiptButtonCell.appendChild(emailButton);
            row.appendChild(receiptButtonCell);
        } else {
            const returnButtonCell = document.createElement('td');
            const returnButton = document.createElement('button');
            returnButton.textContent = 'Devolver';
            returnButton.addEventListener('click', () => openReturnModal(rental));
            returnButtonCell.appendChild(returnButton);
            row.appendChild(returnButtonCell);
        }

        rentalTableBody.appendChild(row);
    });
}

function generateReceipt(rental) {
    const { jsPDF } = window.jspdf;
    const doc = new jsPDF();

    const customerName = capitalizeFirstLetters(rental.cliente.split(" - ")[1]);
    doc.text("===== RECIBO DE ALUGUEL =====", 10, 10);
    doc.text("Veículo: " + rental.veiculo, 10, 20);
    doc.text("Cliente: " + customerName, 10, 30);
    doc.text("Agência de Retirada: " + rental.agenciaRetirada, 10, 40);
    doc.text("Agência de Devolução: " + rental.agenciaDevolucao, 10, 50);
    doc.text("Data de Retirada: " + rental.dataRetirada, 10, 60);
    doc.text("Data de Devolução: " + rental.dataDevolucao, 10, 70);
    doc.text("Custo Total: R$ " + rental.custoTotal, 10, 80);
    doc.text("=============================", 10, 90);
    doc.save('recibo.pdf');
}

function sendRentalReceipt(rental) {
    const customerName = capitalizeFirstLetters(rental.cliente.split(" - ")[1]);
    const customerEmail = rental.cliente.split(" - ")[2];
    const templateParams = {
        veiculo: rental.veiculo,
        nome_do_cliente: customerName,
        agencia_retirada: rental.agenciaRetirada,
        agencia_devolucao: rental.agenciaDevolucao,
        data_retirada: rental.dataRetirada,
        data_devolucao: rental.dataDevolucao,
        custo_total: rental.custoTotal,
        to_email: customerEmail
    };

    emailjs.send('service_h01md0f', 'template_qgzhnuj', templateParams)
        .then(function(response) {
            alert('Recibo enviado com sucesso para o e-mail do cliente!');
        }, function(error) {
            console.log('Erro ao enviar o e-mail:', error);
            alert('Erro ao enviar o recibo. Tente novamente mais tarde.');
        });
}

function openReturnModal(rental) {
    document.getElementById('pickupAgencyReturn').textContent = rental.agenciaRetirada;
    document.getElementById('customerReturn').textContent = rental.cliente;
    document.getElementById('vehicleReturn').textContent = rental.veiculo;
    document.getElementById('rentalDateReturn').textContent = rental.dataRetirada;

    const returnForm = document.getElementById('returnForm');

    returnForm.removeEventListener('submit', submitHandler);

    function submitHandler(event) {
        event.preventDefault();
        const vehiclePlate = rental.veiculo.split(" - ")[0];
        const agencyPickupName = rental.agenciaRetirada.split(" - ")[0];
        const agencyPickupAddress = rental.agenciaRetirada.split(" - ")[1];
        const returnDate = document.getElementById('returnDate').value;
        const agencyReturnName = document.getElementById('agencyReturn').value.split(" / ")[0];
        const agencyReturnAddress = document.getElementById('agencyReturn').value.split(" / ")[1];
        const customerDocument = rental.cliente.split(" - ")[0];

        fetchRent(vehiclePlate, agencyPickupName, agencyPickupAddress, rental.dataRetirada, agencyReturnName, agencyReturnAddress, formatDate(returnDate), customerDocument);
        document.getElementById('returnModal').style.display = 'none';
        loadRentals()
    }

    returnForm.addEventListener('submit', submitHandler);
    document.getElementById('returnModal').style.display = 'block';
}

document.getElementById('closeModal').addEventListener('click', function () {
    document.getElementById('returnModal').style.display = 'none';
});

