document.addEventListener("DOMContentLoaded", function () {
    getAllVehicles();

});

let allVehicles = []

function getAllVehicles() {
    fetch("/vehicles")
        .then(response => response.text())
        .then(data => {
            console.log(data)
            const vehicles = data.split('\n').map(vehicle => {
                const [tipo, marca, placa, modelo, ano, disponivel, nomeAgencia, enderecoAgencia] = vehicle.split(' - ');
                console.log(nomeAgencia)
                console.log(enderecoAgencia)
                return {tipo, marca, placa, modelo, ano, disponivel, nomeAgencia, enderecoAgencia};
            });
            console.log(vehicles)
            displayVehicles(vehicles);
        })
        .catch(error => {
            console.error('Erro ao carregar os veículos:', error);
        });
}

function displayVehicles(vehicles) {


    vehicles.forEach(vehicle => {

        createVehicleCard(vehicle)
        RentForm()


    });
}

function createVehicleCard(vehicle) {
    const modelo = vehicle.modelo;
    const ano = vehicle.ano
    const tipo = vehicle.tipo;
    const placa = vehicle.placa;
    const marca = vehicle.marca;
    const disponivel = vehicle.disponivel;
    const agencyName = vehicle.nomeAgencia.replace("{", "")
    const agencyAddress = vehicle.enderecoAgencia.replace("}", "");

    const vehicle_agency_name = document.createElement("h1")
    vehicle_agency_name.id = "vehicle_agency_name"
    vehicle_agency_name.setAttribute("style", "display:none;")
    vehicle_agency_name.innerText = agencyName

    const vehicle_agency_address = document.createElement("h1")
    vehicle_agency_address.id = "vehicle_agency_address"
    vehicle_agency_address.setAttribute("style", "display:none")
    vehicle_agency_address.innerText = agencyAddress

    const vehicle_plate = document.createElement("h1")
    vehicle_plate.id= "placa"
    vehicle_plate.setAttribute("style", "display:none")
    vehicle_plate.innerText = placa

    const vehicle_container = document.getElementById('vehicle_container');

    const vehicle_card = document.createElement('div');
    vehicle_card.className = "vehicle_card";

    const vehicle_model = document.createElement('h1');
    vehicle_model.className = "vehicle_model";
    vehicle_model.id = "vehicle_model"
    vehicle_model.innerText = modelo;

    const vehicle_brand = document.createElement('h2');
    vehicle_brand.className = "vehicle_brand";
    vehicle_brand.innerText = marca;

    const vehicle_year = document.createElement('h2');
    vehicle_year.className = "vehicle_year";
    vehicle_year.id = "vehicle_year"
    vehicle_year.innerText = "Ano: " + ano;

    const vehicle_status = document.createElement('h2');
    vehicle_status.className = "vehicle_status";
    vehicle_status.innerText = disponivel;

    if (disponivel === "Disponível") {
        vehicle_status.setAttribute("style", "background-color: mediumseagreen; color:white;");
    } else {
        vehicle_status.setAttribute("style", "background-color: gray; color:white;");
    }

    const vehicle_type = document.createElement('h3');
    vehicle_type.className = "vehicle_type";
    vehicle_type.innerText = tipo;

    const rent_vehicle_button = document.createElement('button');
    rent_vehicle_button.id = "rent_vehicle_button";

    rent_vehicle_button.innerText = "Alugar";
    if (disponivel !== "Disponível") {
        rent_vehicle_button.setAttribute("style", "pointer-events:none; background-color: gray");
    }

    vehicle_card.appendChild(vehicle_plate)
    vehicle_card.appendChild(vehicle_agency_name)
    vehicle_card.appendChild(vehicle_agency_address)
    vehicle_card.appendChild(vehicle_model);
    vehicle_card.appendChild(vehicle_brand);
    vehicle_card.appendChild(vehicle_year);
    vehicle_card.appendChild(vehicle_status);
    vehicle_card.appendChild(vehicle_type);
    vehicle_card.appendChild(rent_vehicle_button);

    vehicle_container.appendChild(vehicle_card);
}

function RentForm() {
    const modelo = document.getElementById("vehicle_model").innerText
    const ano = document.getElementById("vehicle_year").innerText
    const rent_vehicle_button = document.getElementById("rent_vehicle_button")
    rent_vehicle_button.addEventListener("click", event => {
        const body = document.getElementById("general_container");
        const bg_overlap = document.createElement("div");
        bg_overlap.className = "bg_overlap";
        bg_overlap.setAttribute("style",
            "position: fixed; " +
            "background-color: rgba(255, 255, 255, 0.1); " +
            "width: 100vw; height: 100vh; " +
            "top: 0; " +
            "left: 0; " +
            "z-index: 9999; " +
            "backdrop-filter: blur(10px); " +
            "box-shadow: 0 4px 6px rgba(0,0,0,0.1); " +
            "border: 1px solid rgba(255, 255, 255, 0.18);"
        );

        body.appendChild(bg_overlap);

        const rent_form = document.createElement("form");
        rent_form.className = "rent_form";

        const rent_date = document.createElement("input");
        rent_date.id = "rent_date"
        rent_date.type = "date";

        const vehicle_form_model = document.createElement("h1");
        vehicle_form_model.innerText = modelo;

        const vehicle_form_year = document.createElement("h2");
        vehicle_form_year.innerText = ano;

        const exit = document.createElement("a");
        exit.className = "exit_button";
        exit.innerText = "x";
        exit.setAttribute("style", "cursor:pointer");

        const rent_vehicle_submit = document.createElement("button");
        rent_vehicle_submit.id = "rent_vehicle_submit"
        rent_vehicle_submit.innerText = "Alugar";


        rent_form.appendChild(exit);
        rent_form.appendChild(vehicle_form_model);
        rent_form.appendChild(vehicle_form_year);
        rent_form.appendChild(rent_date);
        rent_form.appendChild(rent_vehicle_submit);
        bg_overlap.appendChild(rent_form);

        exit.addEventListener("click", event => {
            bg_overlap.style.display = "none";

        });

        rent_vehicle_submit.addEventListener("click", event =>{
            rentVehicle()
        })

    })
}

function rentVehicle() {
    const rent_date = document.getElementById("rent_date")
    const placa = document.getElementById("placa").innerText
    const agencyName = document.getElementById("vehicle_agency_name").innerText
    const agencyAddress = document.getElementById("vehicle_agency_address").innerText
    const userId = document.cookie.split(";")[0].split(":")[1]


        const [year, month, day] = rent_date.value.split("-")
        const dataFormatada = `${day}/${month}/${year}`


        fetch("/rent", {
            method: "POST",
            body: `${placa} / ${agencyName} / ${agencyAddress} / ${dataFormatada} / ${agencyName} / ${agencyAddress} / ${dataFormatada} / ${userId}`,
            headers: {
                'Content-Type': 'application/json'
            }
        })
            .then(response => response.json())
            .then(data => {
                if (data.success) {
                    alert("Aoba, deu bom!");
                } else {
                    alert("Vish...");
                }
            })
            .catch(error => {
                console.error("Erro na requisição:", error);
                alert("Erro ao realizar a requisição.");
            });

}