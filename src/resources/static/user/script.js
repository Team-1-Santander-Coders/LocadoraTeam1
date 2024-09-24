document.getElementById('userForm').addEventListener('submit', function(event) {
    event.preventDefault();
    const name = document.getElementById('name').value;
    const address = document.getElementById('address').value;
    const password = document.getElementById('password').value;
    const email = document.getElementById('email').value;
    const phone = document.getElementById('phone').value;
    const documentValue = document.getElementById('documentValue').value;
    const tipo = document.getElementById('tipo').value;

    fetch('/user', {
        method: 'POST',
        body: `${name},${address},${password},${email}, ${phone}, ${documentValue}, ${tipo}`

    }).then(response => {
        if (response.ok) {
            alert('Usuario cadastrado com sucesso!');
            document.getElementById('userForm').reset()
        } else {
            alert(response.statusText);
        }
    });
});