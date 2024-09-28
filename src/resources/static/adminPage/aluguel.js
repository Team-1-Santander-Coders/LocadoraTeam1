document.addEventListener("DOMContentLoaded", function () {
    loadCustomers();
    loadAvailableVehicles();
    loadAgencies();

    document.getElementById('rentalForm').addEventListener('submit', function (event) {
        event.preventDefault();

        const customerData = document.getElementById('customer').value.split(" / ");
        const vehicleData = document.getElementById('vehicle').value.split(" / ");
        const pickupAgencyData = document.getElementById('pickupAgency').value.split(" / ");
        const dropoffAgencyData = document.getElementById('dropoffAgency').value.split(" / ");
        const startDate = document.getElementById('startDate').value;
        const endDate = document.getElementById('endDate').value;

        const customerName = customerData[0];
        const vehiclePlaca = vehicleData[0];
        const pickupAgencyName = pickupAgencyData[0];
        const pickupAgencyAddress = pickupAgencyData[1];
        const dropoffAgencyName = dropoffAgencyData[0];
        const dropoffAgencyAddress = dropoffAgencyData[1];

        fetch('/rental', {
            method: 'POST',
            body: `${customerName} / ${vehiclePlaca} / ${pickupAgencyName} / ${pickupAgencyAddress} / ${dropoffAgencyName} / ${dropoffAgencyAddress} / ${startDate} / ${endDate}`,
            credentials: 'include',
        }).then(response => {
            if (response.ok) {
                alert('Aluguel criado com sucesso!');
                document.getElementById('rentalForm').reset();
            } else {
                alert('Erro ao criar aluguel.');
            }
        });
    });
});

function loadCustomers() {
    fetch('/users')
        .then(response => response.json())
        .then(customers => {
            const customerSelect = document.getElementById('customer');
            customerSelect.innerHTML = '';

            customers.forEach(customer => {
                const option = document.createElement('option');
                option.textContent = `${customer.name} / ${customer.email}`;
                option.value = `${customer.name} / ${customer.email}`;
                customerSelect.appendChild(option);
            });
        })
        .catch(error => {
            console.error('Erro ao carregar clientes:', error);
            alert('Erro ao carregar clientes.');
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

function loadAgencies() {
    fetch('/agencies')
        .then(response => response.json())
        .then(agencies => {
            const pickupAgencySelect = document.getElementById('pickupAgency');
            const dropoffAgencySelect = document.getElementById('dropoffAgency');
            pickupAgencySelect.innerHTML = '';
            dropoffAgencySelect.innerHTML = '';

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
