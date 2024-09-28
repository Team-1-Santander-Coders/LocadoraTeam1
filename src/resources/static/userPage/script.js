document.addEventListener("DOMContentLoaded", function () {
    getAllVehicles();

});

let allVehicles = []

function getAllVehicles() {
    fetch("/vehicles")
        .then(response => response.text())
        .then(data => {

            const vehicles = data.split('\n').map(vehicle => {
                const [tipo, marca, placa, modelo, ano, disponivel, agency] = vehicle.split(' - ');
                const [agencyName, agencyAddress] = agency.replace('{', '').replace('}', '').split(' - ');
                console.log(agencyName)
                console.log(agencyAddress)
                return {tipo, marca, placa, modelo, ano, disponivel, agencyName, agencyAddress};
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

        const modelo = vehicle.modelo;
        const ano = vehicle.ano
        const tipo = vehicle.tipo;
        const placa = vehicle.placa;
        const marca = vehicle.marca;
        const disponivel = vehicle.disponivel;
        const agencyName = vehicle.agencyName;
        const agencyAddress = vehicle.agencyAddress;

        const vehicle_card = document.createElement('div');
        vehicle_card.className = "vehicle_card";

        const vehicle_model = document.createElement('h1');
        vehicle_model.className = "vehicle_model";
        vehicle_model.innerText = modelo;

        const vehicle_brand = document.createElement('h2');
        vehicle_brand.className = "vehicle_brand";
        vehicle_brand.innerText = marca;

        const vehicle_year = document.createElement('h2');
        vehicle_year.className = "vehicle_year";
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
        rent_vehicle_button.className = "rent_vehicle_button";
        rent_vehicle_button.innerText = "Alugar";
        if (disponivel !== "Disponível") {
            rent_vehicle_button.setAttribute("style", "pointer-events:none; background-color: gray");
        }

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
            rent_date.type = "date";

            const vehicle_form_model = document.createElement("h1");
            vehicle_form_model.innerText = modelo;

            const vehicle_form_year = document.createElement("h2");
            vehicle_form_year.innerText = ano;

            const exit = document.createElement("a");
            exit.className  = "exit_button";
            exit.innerText = "x";
            exit.setAttribute("style", "cursor:pointer");

            const rent_vehicle_submit = document.createElement("button");
            rent_vehicle_submit.innerText = "Alugar";
            rent_vehicle_submit.addEventListener("click", event => {
                event.preventDefault();

                const [year, month, day] = rent_date.value.split("-")
                const dataFormatada = `${day}/${month}/${year}`
                const agencyAddress = "aoba"

                fetch("/rent", {
                    method: "POST",
                    body: JSON.stringify({
                        placa,
                        agencyName,
                        agencyAddress,
                        dataFormatada,
                    }),
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
            });

            rent_form.appendChild(exit);
            rent_form.appendChild(vehicle_form_model);
            rent_form.appendChild(vehicle_form_year);
            rent_form.appendChild(rent_date);
            rent_form.appendChild(rent_vehicle_submit);

            bg_overlap.appendChild(rent_form);

            exit.addEventListener("click", event => {
                bg_overlap.style.display = "none";
            });
        });

        vehicle_card.appendChild(vehicle_model);
        vehicle_card.appendChild(vehicle_brand);
        vehicle_card.appendChild(vehicle_year);
        vehicle_card.appendChild(vehicle_status);
        vehicle_card.appendChild(vehicle_type);
        vehicle_card.appendChild(rent_vehicle_button);

        vehicle_container.appendChild(vehicle_card);
    });
}