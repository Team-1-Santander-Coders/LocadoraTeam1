if (window.location.href.includes("veiculo")){
    document.addEventListener("DOMContentLoaded", function() {
        loadVehicles();
        loadAgencies();

        document.getElementById('search').addEventListener('input', filterVehicles);
        document.getElementById('disponibility').addEventListener('change', filterVehicles);
    });

    let allVehicles = [];

    document.getElementById('vehicleForm').addEventListener('submit', function(event) {
        event.preventDefault();
        const agencyData = document.getElementById('agency').value.split(" / ");
        const type = document.getElementById('type').value;
        const placa = document.getElementById('placa').value.replace(/[^a-z0-9]/gi, '');
        const modelo = document.getElementById('modelo').value;
        const marca = document.getElementById('marca').value;
        const ano = document.getElementById('ano').value;
        const agencyName = agencyData[0];
        const agencyAddress = agencyData[1];


        fetch('/vehicle', {
            method: 'POST',
            body: `${type} / ${marca} / ${placa} / ${modelo} / ${marca} / ${ano} / ${agencyName} / ${agencyAddress}`,
            credentials: 'include',
        }).then(response => {
            if (response.ok) {
                alert('Veículo cadastrado com sucesso!');
                loadVehicles();
                document.getElementById('vehicleForm').reset()
            } else {
                alert('Já existe um veículo cadastrado com essa placa.');
            }
        });
    });

    function loadVehicles() {
        fetch('http://localhost:8000/vehicles', {
            method: 'GET',
            credentials: 'include',
        })
            .then(response => {
                if (!response.ok) {
                    throw new Error(`HTTP error! status: ${response.status}`);
                }
                return response.text();
            })
            .then(data => {
                const vehicleList = document.getElementById('vehicleList');
                vehicleList.innerHTML = '';

                if (data.trim() === '') {
                    document.getElementById('noVehiclesMessage').style.display = 'block';
                } else {
                    document.getElementById('noVehiclesMessage').style.display = 'none';

                    allVehicles = data.split('\n').map(vehicle => {
                        const [tipo, marca, placa, modelo, ano, disponivel, agency] = vehicle.split(' - ');
                        const [agencyName, agencyAddress] = agency.replace('{', '').replace('}', '').split(' - ');
                        return { tipo, marca, placa, modelo, ano, disponivel, agencyName, agencyAddress };
                    });

                    displayVehicles(allVehicles);
                }
            })
            .catch(error => {
                console.error('Erro ao carregar os veículos:', error);
                document.getElementById("vehicleList").textContent = `Erro ao carregar veículos`;
            });
    }

    function displayVehicles(vehicles) {
        const vehicleList = document.getElementById('vehicleList');
        vehicleList.innerHTML = '';

        if (vehicles.length === 0) {
            vehicleList.innerHTML = "<li>Nenhum veículo encontrado</li>";
            return;
        }

        vehicles.forEach(vehicle => {
            const li = document.createElement('li');
            li.textContent = `${vehicle.tipo} - ${vehicle.placa} - ${vehicle.modelo} - ${vehicle.ano} - ${vehicle.disponivel === 'Disponível' ? 'Disponível' : 'Indisponível'}`;
            vehicleList.appendChild(li);

            const deleteButton = document.createElement('button');
            deleteButton.textContent = 'Deletar';
            deleteButton.onclick = function() {
                deleteVehicle(vehicle.placa);
            };
            li.appendChild(deleteButton);
        });
    }

    function filterVehicles() {
        const searchTerm = document.getElementById('search').value.toLowerCase();

        const filteredVehicles = allVehicles.filter(vehicle =>
            vehicle.placa.toLowerCase().includes(searchTerm) ||
            vehicle.modelo.toLowerCase().includes(searchTerm) ||
            vehicle.tipo.toLowerCase().includes(searchTerm) ||
            vehicle.ano.includes(searchTerm)
        );

        if (document.getElementById('disponibility').value === '') {
            displayVehicles(filteredVehicles);
        }

        if (document.getElementById('disponibility').value === 'true') {
            const filteredVehiclesByDisponibility = filteredVehicles.filter(vehicle =>
                vehicle.disponivel === 'Disponível'
            );
            displayVehicles(filteredVehiclesByDisponibility)
        }

        if (document.getElementById('disponibility').value === 'false') {
            const filteredVehiclesByDisponibility = filteredVehicles.filter(vehicle =>
                vehicle.disponivel === 'Indisponível'
            );
            displayVehicles(filteredVehiclesByDisponibility)
        }
    }

    function deleteVehicle(placa) {
        fetch('/vehicle/delete', {
            method: 'DELETE',
            headers: {
                'Content-Type': 'application/json'
            },
            credentials: 'include',
            body: placa
        }).then(response => {
            if (!response.ok) {
                return response.text().then(text => {
                    throw new Error(text || 'Erro ao deletar veículo.');
                });
            }
            alert('Veículo deletado com sucesso!');
            loadVehicles();
        }).catch(error => {
            console.error('Erro:', error);
            alert('Erro ao deletar veículo: ' + error.message);
        });
    }

    function loadAgencies() {
        fetch('/agencies')
            .then(response => {
                if (!response.ok) throw new Error('Erro ao buscar agências');
                return response.json();
            })
            .then(agencies => {
                const agencyList = document.getElementById('agency');
                agencyList.innerHTML = '';

                if (agencies.length === 0) {
                } else {
                    agencies.forEach(agency => {
                        const option = document.createElement('option');
                        option.textContent = `${agency.name} - ${agency.address}`;
                        option.value = `${agency.name} / ${agency.address}`
                        agencyList.appendChild(option);
                    });
                }
            })
            .catch(error => {
                console.error('Erro:', error);
                alert('Erro ao carregar agências.');
            });
    }
}

