document.addEventListener("DOMContentLoaded", function() {
    getAllVehicles();

});

let allVehicles = []

function getAllVehicles(){
    fetch("/vehicles")
        .then(response => response.text())
        .then(data => {

            const vehicles = data.split('\n').map(vehicle => {
                const [tipo, marca, placa, modelo, ano, disponivel] = vehicle.split(' - ');
                return { tipo, marca, placa, modelo, ano, disponivel };
            });
            console.log(vehicles)
            displayVehicles(vehicles);
        })
        .catch(error => {
            console.error('Erro ao carregar os veículos:', error);
        });
}

function displayVehicles(vehicles) {
    const vehicle_container = document.getElementById('vehicle_container');

    if (vehicles.length === 0) {
        vehicle_container.innerHTML = "<h1>Sem veículos cadastrados</h1>";
        return;
    }

    vehicles.forEach(vehicle => {
        const vehicle_container = document.getElementById("vehicle_container")
        const vehicle_card = document.createElement('div')
        vehicle_card.className = "vehicle_card"

        const vehicle_model = document.createElement('h1')
        vehicle_model.className = "vehicle_model"
        vehicle_model.innerText = vehicle.modelo

        const vehicle_brand = document.createElement('h2')
        vehicle_brand.className = "vehicle_brand"
        vehicle_brand.innerText = vehicle.marca

        const vehicle_year = document.createElement('h2')
        vehicle_year.className = "vehicle_year"
        vehicle_year.innerText = "Ano: " + vehicle.ano

        const vehicle_status = document.createElement('h2')
        vehicle_status.className = "vehicle_status"
        vehicle_status.innerText = vehicle.disponivel

        if(vehicle.disponivel === "Disponível"){
            vehicle_status.setAttribute("style", "background-color: mediumseagreen; color:white;")
        } else{
            vehicle_status.setAttribute("style", "background-color: gray; color:white;")
        }

        const vehicle_type = document.createElement('h3')
        vehicle_type.className = "vehicle_type"
        vehicle_type.innerText = vehicle.tipo


        const rent_vehicle = document.createElement('button')
        rent_vehicle.className = "rent_vehicle_button"
        rent_vehicle.innerText = "Alugar"
        if(vehicle.disponivel !== "Disponível"){
            rent_vehicle.setAttribute("style", "pointer-events:none; background-color: gray")
        }

        vehicle_card.appendChild(vehicle_model)
        vehicle_card.appendChild(vehicle_brand)
        vehicle_card.appendChild(vehicle_year)
        vehicle_card.appendChild(vehicle_type)
        vehicle_card.appendChild(vehicle_status)

        vehicle_card.appendChild(rent_vehicle)
        vehicle_container.appendChild(vehicle_card)

    });
}