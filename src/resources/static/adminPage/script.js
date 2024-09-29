document.addEventListener("DOMContentLoaded", function () {
    let allVehicles = [];

    function loadVehicles() {
        fetch('/vehicles')
            .then(response => response.text())
            .then(data => {
                if (data.trim() === '') {
                    console.log('Nenhum veículo encontrado.');
                } else {
                    allVehicles = data.split('\n').map(vehicle => {
                        const [tipo, placa, marca, modelo, ano, disponivel] = vehicle.split(' - ');
                        return { tipo, placa, marca, modelo, ano, disponivel };
                    });

                    generateChart(allVehicles);
                }
            })
            .catch(error => {
                console.error('Erro ao carregar os veículos:', error);
            });
    }

    function generateChart(vehicles) {
        const availableCount = { Carro: 0, Moto: 0, Caminhão: 0 };
        const unavailableCount = { Carro: 0, Moto: 0, Caminhão: 0 };

        vehicles.forEach(vehicle => {
            if (vehicle.disponivel === 'Disponível') {
                availableCount[vehicle.tipo]++;
            } else {
                unavailableCount[vehicle.tipo]++;
            }
        });

        const ctx = document.getElementById('vehicleChart').getContext('2d');
        new Chart(ctx, {
            type: 'bar',
            data: {
                labels: ['Carros', 'Motos', 'Caminhões'],
                datasets: [
                    {
                        label: 'Disponível',
                        data: [availableCount.Carro, availableCount.Moto, availableCount.Caminhão],
                        backgroundColor: 'rgba(52, 152, 219, 0.9)',
                    },
                    {
                        label: 'Indisponível',
                        data: [unavailableCount.Carro, unavailableCount.Moto, unavailableCount.Caminhão],
                        backgroundColor: 'rgba(231, 76, 60, 0.9)',
                    }
                ]
            },
            options: {
                responsive: true,
                plugins: {
                    legend: {
                        position: 'top',
                    },
                },
                scales: {
                    x: {
                        stacked: true,
                    },
                    y: {
                        stacked: true,
                        beginAtZero: true,
                    }
                }
            }
        });
    }

    loadVehicles();

    let allRentals = [];
    let totalCost = 0;

    function loadRentals() {
        fetch('/rentals')
            .then(response => response.text())
            .then(text => {
                try {
                    const cleanedText = text.trim();
                    const rentals = JSON.parse(cleanedText);

                    if (Array.isArray(rentals)) {
                        allRentals = rentals;
                    } else {
                        allRentals = [rentals];
                    }

                    totalCost = allRentals.reduce((acc, rental) => acc + parseFloat(rental.custoTotal), 0);

                    generateRentalChart(allRentals);
                    updateTotalCost(totalCost);
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

    function consultarTipoPorPlaca(placa) {
        const vehicle = allVehicles.find(vehicle => vehicle.marca.toLowerCase() === placa.toLowerCase());
        return vehicle ? vehicle.tipo : 'Veículo não encontrado';
    }

    function generateRentalChart(rentals) {
        const returnedCount = { Carro: 0, Moto: 0, Caminhão: 0 };
        const openCount = { Carro: 0, Moto: 0, Caminhão: 0 };
        console.log(allVehicles)

        rentals.forEach(rental => {
            const vehicleType = detectVehicleType(consultarTipoPorPlaca(rental.veiculo.split(" - ")[0]));

            if (rental.situacao === 'Em aberto.') {
                openCount[vehicleType]++;
            } else {
                returnedCount[vehicleType]++;
            }
        });

        const ctx = document.getElementById('rentalChart').getContext('2d');
        new Chart(ctx, {
            type: 'bar',
            data: {
                labels: ['Aluguel de carros', 'Aluguel de motos', 'Aluguel de caminhões'],
                datasets: [
                    {
                        label: 'Em aberto',
                        data: [openCount.Carro, openCount.Moto, openCount.Caminhão],
                        backgroundColor: 'rgba(52, 152, 219, 0.9)',
                    },
                    {
                        label: 'Devolvido',
                        data: [returnedCount.Carro, returnedCount.Moto, returnedCount.Caminhão],
                        backgroundColor: 'rgba(46, 204, 113, 0.9)',
                    }
                ]
            },
            options: {
                responsive: true,
                plugins: {
                    legend: {
                        position: 'top',
                    },
                },
                scales: {
                    x: {
                        stacked: true,
                    },
                    y: {
                        stacked: true,
                        beginAtZero: true,
                    }
                }
            }
        });
    }

        function detectVehicleType(veiculo) {
            if (veiculo.toLowerCase().includes('carro')) return 'Carro';
            if (veiculo.toLowerCase().includes('moto')) return 'Moto';
            if (veiculo.toLowerCase().includes('caminhão')) return 'Caminhão';
            return 'Outro';
        }

        function updateTotalCost(cost) {
            const totalCostElement = document.getElementById('totalCost');
            totalCostElement.textContent = `Valor total dos aluguéis: R$ ${cost.toFixed(2)}`.replace(".", ",");
        }

        loadRentals();
});
