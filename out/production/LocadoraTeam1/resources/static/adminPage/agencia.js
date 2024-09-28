document.addEventListener('DOMContentLoaded', () => {
    loadAgencies();

    document.getElementById('addAgencyForm').addEventListener('submit', function(event) {
        event.preventDefault();

        const name = document.getElementById('name').value;
        const address = document.getElementById('address').value;

        fetch('/agency', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: `${name} / ${address}`,
            credentials: 'include',
        })
            .then(response => {
                if (!response.ok) {
                    return response.text().then(text => {
                        throw new Error(`HTTP error! status: ${response.status}, message: ${text}`);
                    });
                }
                return response.text();
            })
            .then(message => {
                alert(message);
                loadAgencies();
            })
            .catch(error => {
                console.error('Erro:', error);
                alert(`Erro ao editar agência: ${error.message}`);
            });
    });
});

function loadAgencies() {
    fetch('/agencies')
        .then(response => {
            if (!response.ok) throw new Error('Erro ao buscar agências');
            return response.json();
        })
        .then(agencies => {
            const agencyList = document.getElementById('agencyList');
            agencyList.innerHTML = '';

            if (agencies.length === 0) {
                document.getElementById('noAgenciesMessage').style.display = 'block';
            } else {
                document.getElementById('noAgenciesMessage').style.display = 'none';
                agencies.forEach(agency => {
                    const li = document.createElement('li');
                    li.textContent = `${agency.name} - ${agency.address}`;
                    li.innerHTML += ` <button onclick="openEditDialog('${agency.name}', '${agency.address}')">Editar</button>`;
                    agencyList.appendChild(li);
                });
            }
        })
        .catch(error => {
            console.error('Erro:', error);
            alert('Erro ao carregar agências.');
        });
}

function openEditDialog(oldName, oldAddress) {
    document.getElementById('oldName').value = oldName;
    document.getElementById('oldAddress').value = oldAddress;
    document.getElementById('editAgencyDialog').style.display = 'block';
}

function closeEditDialog() {
    document.getElementById('editAgencyDialog').style.display = 'none';
}

document.getElementById('editAgencyForm').addEventListener('submit', function(event) {
    event.preventDefault();

    const oldName = document.getElementById('oldName').value;
    const oldAddress = document.getElementById('oldAddress').value;
    const newName = document.getElementById('newName').value;
    const newAddress = document.getElementById('newAddress').value;

    const body = `${oldName} / ${oldAddress} / ${newName} / ${newAddress}`;

    fetch('/agency/edit', {
        method: 'PUT',
        headers: {
            'Content-Type': 'application/json',
        },
        body: body,
        credentials: 'include',
    })
        .then(response => {
            if (!response.ok) {
                return response.text().then(text => {
                    throw new Error(`HTTP error! status: ${response.status}, message: ${text}`);
                });
            }
            return response.text();
        })
        .then(message => {
            alert(message);
            loadAgencies();
            closeEditDialog();
        })
        .catch(error => {
            console.error('Erro:', error);
            alert(`Erro ao editar agência: ${error.message}`);
        });
});
