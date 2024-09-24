document.addEventListener("DOMContentLoaded", function() {
    loadVehicles(); // Carrega a lista de veículos quando a página é carregada

    // Adiciona um evento de input no campo de pesquisa para filtrar conforme o usuário digita
    document.getElementById('search').addEventListener('input', filterVehicles);
    document.getElementById('disponibility').addEventListener('change', filterVehicles);
});

let allVehicles = [];


document.getElementById('vehicleForm').addEventListener('submit', function(event) {
    event.preventDefault();
    const type = document.getElementById('type').value;
    const placa = document.getElementById('placa').value.replace(/[^a-z0-9]/gi, '');
    const modelo = document.getElementById('modelo').value;
    const marca = document.getElementById('marca').value;
    const ano = document.getElementById('ano').value;

    fetch('/vehicle', {
        method: 'POST',
        body: `${type},${placa},${modelo},${marca},${ano}`
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
    fetch('/vehicles') // Faz um GET para a rota que lista todos os veículos
        .then(response => response.text()) // Lê a resposta como texto simples
        .then(data => {
            const vehicleList = document.getElementById('vehicleList');
            vehicleList.innerHTML = ''; // Limpa a lista de veículos

            if (data.trim() === '') {
                document.getElementById('noVehiclesMessage').style.display = 'block'; // Mostra a mensagem "Sem veículos"
            } else {
                document.getElementById('noVehiclesMessage').style.display = 'none'; // Esconde a mensagem

                // Divide os veículos pelo caractere de nova linha
                allVehicles = data.split('\n').map(vehicle => {
                    // Divide cada linha pelos delimitadores " - " e retorna um objeto
                    const [tipo, placa, modelo, ano, disponivel] = vehicle.split(' - ');
                    return { tipo, placa, modelo, ano, disponivel };
                });

                // Exibe os veículos na lista inicialmente
                displayVehicles(allVehicles); // Mostra todos os veículos quando carregado
            }
        })
        .catch(error => {
            console.error('Erro ao carregar os veículos:', error);
            document.getElementById("vehicleList").textContent = "Erro ao carregar veículos.";
        });
}

function displayVehicles(vehicles) {
    const vehicleList = document.getElementById('vehicleList');
    vehicleList.innerHTML = ''; // Limpa a lista de veículos antes de renderizar

    if (vehicles.length === 0) {
        vehicleList.innerHTML = "<li>Nenhum veículo encontrado</li>"; // Exibe uma mensagem se não houver veículos após a pesquisa
        return;
    }

    // Para cada veículo, cria um item da lista (li) e o adiciona ao <ul>
    vehicles.forEach(vehicle => {
        const li = document.createElement('li');
        li.textContent = `${vehicle.tipo} - ${vehicle.placa} - ${vehicle.modelo} - ${vehicle.ano} - ${vehicle.disponivel === 'Disponível' ? 'Disponível' : 'Indisponível'}`;
        vehicleList.appendChild(li);

        // Adiciona um botão de deletar para cada veículo
        const deleteButton = document.createElement('button');
        deleteButton.textContent = 'Deletar';
        deleteButton.onclick = function() {
            deleteVehicle(vehicle.placa); // Chama a função de deletar, passando a placa do veículo
        };
        li.appendChild(deleteButton);
    });
}

function filterVehicles() {
    const searchTerm = document.getElementById('search').value.toLowerCase(); // Obtém o valor do campo de pesquisa

    // Filtra a lista de veículos com base na placa, modelo, tipo ou ano
    const filteredVehicles = allVehicles.filter(vehicle =>
        vehicle.placa.toLowerCase().includes(searchTerm) || // Verifica se a placa contém o termo pesquisado
        vehicle.modelo.toLowerCase().includes(searchTerm) || // Verifica se o modelo contém o termo pesquisado
        vehicle.tipo.toLowerCase().includes(searchTerm) || // Verifica se o tipo contém o termo pesquisado
        vehicle.ano.includes(searchTerm) // Verifica se o ano contém o termo pesquisado
    );

    if (document.getElementById('disponibility').value === '') {
        // Exibe apenas os veículos filtrados
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
        body: placa
    }).then(response => {
        if (response.ok) {
            alert('Veículo deletado com sucesso!');
            loadVehicles();
        } else {
            alert('Erro ao deletar veículo.');
        }
    });
}

// Carregar veículos ao iniciar
loadVehicles();
