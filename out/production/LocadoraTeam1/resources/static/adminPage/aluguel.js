document.addEventListener("DOMContentLoaded", function () {
    loadCustomers();
    loadAvailableVehicles()
    loadAgencies();

    document.getElementById('rentalForm').addEventListener('submit', function (event) {
        event.preventDefault();

        const customerData = document.getElementById('customer').value.split(" / ");
        const vehicleData = document.getElementById('vehicle').value.split(" / ");
        const pickupAgencyData = document.getElementById('pickupAgency').value.split(" / ");
        const startDate = formatDate(document.getElementById('startDate').value);
        let endDate = 'Sem data de devolução';

        const customerDocument = customerData[2];
        const vehiclePlaca = vehicleData[1];
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


