let agencyToDelete = { name: '', address: '' };

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

    document.getElementById('search').addEventListener('input', filterAgencies);
    document.getElementById('cancelDelete').addEventListener('click', closeDeleteDialog);
    document.getElementById('confirmDelete').addEventListener('click', confirmDeleteAgency);
});

function loadAgencies() {
    fetch('/agencies')
        .then(response => {
            if (!response.ok) throw new Error('Erro ao buscar agências');
            return response.json();
        })
        .then(agencies => {
            displayAgencies(agencies);
        })
        .catch(error => {
            console.error('Erro:', error);
            alert('Erro ao carregar agências.');
        });
}

function displayAgencies(agencies) {
    const agencyList = document.getElementById('agencyList');
    agencyList.innerHTML = '';

    if (agencies.length === 0) {
        document.getElementById('noAgenciesMessage').style.display = 'block';
    } else {
        document.getElementById('noAgenciesMessage').style.display = 'none';
        agencies.forEach(agency => {
            const li = document.createElement('li');
            li.textContent = `${agency.name} - ${agency.address}`;

            const editButton = document.createElement('button');
            editButton.textContent = 'Editar';
            editButton.onclick = () => openEditDialog(agency.name, agency.address);
            li.appendChild(editButton);

            const deleteButton = document.createElement('button');
            deleteButton.textContent = 'Deletar';
            deleteButton.onclick = () => openDeleteDialog(agency.name, agency.address);
            deleteButton.style.marginLeft = '10px';
            li.appendChild(deleteButton);

            agencyList.appendChild(li);
        });
    }
}

function openDeleteDialog(name, address) {
    agencyToDelete = { name, address };
    document.getElementById('deleteDialog').style.display = 'block';
}

function closeDeleteDialog() {
    document.getElementById('deleteDialog').style.display = 'none';
    agencyToDelete = { name: '', address: '' };
}

function confirmDeleteAgency() {
    const { name, address } = agencyToDelete;
    fetch('/agency/delete', {
        method: 'DELETE',
        headers: {
            'Content-Type': 'text/plain',
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
            closeDeleteDialog();
            loadAgencies();
        })
        .catch(error => {
            console.error('Erro:', error);
            alert(`Erro ao deletar agência: ${error.message}`);
        });
}

function filterAgencies() {
    const searchTerm = document.getElementById('search').value.toLowerCase();
    const agencyItems = document.querySelectorAll('#agencyList li');

    agencyItems.forEach(item => {
        const agencyText = item.textContent.toLowerCase();
        if (agencyText.includes(searchTerm)) {
            item.style.display = '';
        } else {
            item.style.display = 'none';
        }
    });
}
