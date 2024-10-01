(function(){
    emailjs.init("-Z51VkHqRSKon-Gi5");
})();

let allVehicles = []

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
            alert('Operação realizada com sucesso!');
            document.getElementById('rentalForm').reset();
            loadAvailableVehicles();
            loadRentals();
        } else {
            alert('Erro ao realizar operação aluguel.');
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
            allVehicles = data.split('\n');
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
            const emailButton = document.createElement('button');
            emailButton.textContent = 'Enviar E-mail';
            emailButton.style.marginLeft = '10px';
            emailButton.addEventListener('click', () => sendRentalReceipt(rental));
            returnButtonCell.appendChild(emailButton);
            row.appendChild(returnButtonCell);
        }

        rentalTableBody.appendChild(row);
    });
}

function generateReceipt(rental) {
    const { jsPDF } = window.jspdf;
    const doc = new jsPDF();

    const customerName = capitalizeFirstLetters(rental.cliente.split(" - ")[1]);

    const devolucaoDate = new Date(rental.dataDevolucao.split("/").reverse().join("-"));
    devolucaoDate.setDate(devolucaoDate.getDate() + 5);
    const vencimentoDate = devolucaoDate.toLocaleDateString("pt-BR");

    const boletoData = {
        banco: "Banco Fictício - 123",
        cedente: "Locadora Team One",
        sacado: customerName,
        nossoNumero: "98765432101",
        agencia: "5678-9",
        conta: "98765-4",
        linhaDigitavel: "12390.45678 98765.432109 87654.321012 3 12340000015000"
    };

    const barcodeCanvas = document.getElementById("barcodeCanvas");
    JsBarcode(barcodeCanvas, "123904567898765432109876543210123", { format: "CODE128" });

    const pixCode = "00020126360014BR.GOV.BCB.PIX0114+55119999999950203PIX5204000053039865405123.455802BR5925Locadora Team One6009Sao Paulo61080540900062120512********6304ABCD"; // Exemplo de código PIX

    const qr = new QRious({
        value: pixCode,
        size: 100
    });

    const imgData = qr.toDataURL();

    doc.setFontSize(12);
    let yPosition = 10;

    doc.text("===========================================================", 20, yPosition);
    yPosition += 10;

    doc.text("Banco: " + boletoData.banco, 20, yPosition);
    yPosition += 10;

    doc.text("Cedente: " + boletoData.cedente, 20, yPosition);
    yPosition += 10;

    doc.text("Nosso Número: " + boletoData.nossoNumero, 20, yPosition);
    yPosition += 10;

    doc.text("Agência/Código Cedente: " + boletoData.agencia + "/" + boletoData.conta, 20, yPosition);
    yPosition += 10;

    doc.text("Data de Vencimento: " + vencimentoDate, 20, yPosition);
    yPosition += 10;

    doc.text("Valor: R$ " + rental.custoTotal, 20, yPosition);
    yPosition += 10;

    doc.text("===========================================================", 20, yPosition);
    yPosition += 20;

    doc.text("Sacado: " + boletoData.sacado, 20, yPosition);
    yPosition += 10;

    doc.text("Linha Digitável: " + boletoData.linhaDigitavel, 20, yPosition);
    yPosition += 20;

    const barcodeImgData = barcodeCanvas.toDataURL("image/png");
    doc.addImage(barcodeImgData, "PNG", 20, yPosition, 160, 20);
    yPosition += 30;

    doc.text("===========================================================", 20, yPosition);
    yPosition += 10;

    doc.text("Veículo: " + rental.veiculo, 20, yPosition);
    yPosition += 10;

    doc.text("Agência de Retirada: " + rental.agenciaRetirada, 20, yPosition);
    yPosition += 10;

    doc.text("Agência de Devolução: " + rental.agenciaDevolucao, 20, yPosition);
    yPosition += 10;

    doc.text("Data de Retirada: " + rental.dataRetirada, 20, yPosition);
    yPosition += 10;

    doc.text("Data de Devolução: " + rental.dataDevolucao, 20, yPosition);
    yPosition += 10;

    doc.text("===========================================================", 20, yPosition);
    yPosition += 10;

    doc.text("QR Code para pagamento via PIX:", 20, yPosition);
    yPosition += 10;
    doc.addImage(imgData, "PNG", 20, yPosition, 48, 48);

    doc.save('boleto.pdf');
}

function getValorDiariaPelaPlaca(placa) {
    tipo = allVehicles.filter(vehicle => vehicle.includes(placa))[0].split(" - ")[0];
    if (tipo === "Carro") {
        return "150,00"
    }

    if (tipo === "Moto") {
        return "100,00"
    }

    if (tipo === "Caminhão") {
        return "200,00"
    }
}

function sendRentalReceipt(rental) {
    const customerName = capitalizeFirstLetters(rental.cliente.split(" - ")[1]);
    const customerEmail = rental.cliente.split(" - ")[2];
    console.log(rental.situacao)
    if (rental.situacao === "Em aberto."){
        const valor = getValorDiariaPelaPlaca(rental.veiculo.split(" - ")[0]);
        const templateParams = {
            veiculo: rental.veiculo,
            nome_do_cliente: customerName,
            agencia_retirada: rental.agenciaRetirada,
            agencia_devolucao: rental.agenciaDevolucao,
            data_retirada: rental.dataRetirada,
            data_devolucao: rental.dataDevolucao,
            custo_total: valor,
            to_email: customerEmail,
            tipo: "Preço da diária"
        };

        emailjs.send('service_h01md0f', 'template_qgzhnuj', templateParams)
            .then(function(response) {
                alert('Recibo enviado com sucesso para o e-mail do cliente!');
            }, function(error) {
                console.log('Erro ao enviar o e-mail:', error);
                alert('Erro ao enviar o recibo. Tente novamente mais tarde.');
            });
    } else {
        const templateParams = {
            veiculo: rental.veiculo,
            nome_do_cliente: customerName,
            agencia_retirada: rental.agenciaRetirada,
            agencia_devolucao: rental.agenciaDevolucao,
            data_retirada: rental.dataRetirada,
            data_devolucao: rental.dataDevolucao,
            custo_total: rental.custoTotal,
            to_email: customerEmail,
            tipo: "Custo total"
        };

        emailjs.send('service_h01md0f', 'template_qgzhnuj', templateParams)
            .then(function(response) {
                alert('Recibo enviado com sucesso para o e-mail do cliente!');
            }, function(error) {
                console.log('Erro ao enviar o e-mail:', error);
                alert('Erro ao enviar o recibo. Tente novamente mais tarde.');
            });
    }

}

function openReturnModal(rental) {
    currentRental = rental;
    document.getElementById('pickupAgencyReturn').textContent = rental.agenciaRetirada;
    document.getElementById('customerReturn').textContent = rental.cliente;
    document.getElementById('vehicleReturn').textContent = rental.veiculo;
    document.getElementById('rentalDateReturn').textContent = rental.dataRetirada;

    document.getElementById('returnModal').style.display = 'block';
}

function submitHandler(event) {
    event.preventDefault();
    if (!currentRental) return;

    const vehiclePlate = currentRental.veiculo.split(" - ")[0];
    const agencyPickupName = currentRental.agenciaRetirada.split(" - ")[0];
    const agencyPickupAddress = currentRental.agenciaRetirada.split(" - ")[1];
    const returnDate = document.getElementById('returnDate').value;
    const agencyReturnName = document.getElementById('agencyReturn').value.split(" / ")[0];
    const agencyReturnAddress = document.getElementById('agencyReturn').value.split(" / ")[1];
    const customerDocument = currentRental.cliente.split(" - ")[0];

    fetchRent(vehiclePlate, agencyPickupName, agencyPickupAddress, currentRental.dataRetirada, agencyReturnName, agencyReturnAddress, formatDate(returnDate), customerDocument);
    document.getElementById('returnModal').style.display = 'none';
    document.getElementById('returnForm').reset();
    loadRentals();
    currentRental = null;
}

document.getElementById('returnForm').addEventListener('submit', submitHandler);
document.getElementById('closeModal').addEventListener('click', function () {
    document.getElementById('returnModal').style.display = 'none';
});

document.getElementById('closeModal').addEventListener('click', function () {
    document.getElementById('returnModal').style.display = 'none';
});

