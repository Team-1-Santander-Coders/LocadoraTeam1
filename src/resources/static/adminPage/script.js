document.addEventListener("DOMContentLoaded", function() {
    loadVehicles();

    document.getElementById('search').addEventListener('input', filterVehicles);
    document.getElementById('disponibility').addEventListener('change', filterVehicles);
});

let allVehicles = [];


function loadVehicles() {
    fetch('/vehicles')
        .then(response => response.text())
        .then(data => {
            const vehicleList = document.getElementById('vehicleList');
            vehicleList.innerHTML = '';

            if (data.trim() === '') {
                document.getElementById('noVehiclesMessage').style.display = 'block';
            } else {
                document.getElementById('noVehiclesMessage').style.display = 'none';


                allVehicles = data.split('\n').map(vehicle => {
                    const [tipo, placa, marca, modelo, ano, disponivel] = vehicle.split(' - ');
                    return { tipo, placa, marca, modelo, ano, disponivel };
                });

                displayVehicles(allVehicles);
            }
        })
        .catch(error => {
            console.error('Erro ao carregar os veículos:', error);
            document.getElementById("vehicleList").textContent = "Erro ao carregar veículos.";
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
        if (response.ok) {
            alert('Veículo deletado com sucesso!');
            loadVehicles();
        } else {
            alert('Erro ao deletar veículo.');
        }
    }).catch(error => {
        console.error('Erro:', error);
    });
}

loadVehicles();
