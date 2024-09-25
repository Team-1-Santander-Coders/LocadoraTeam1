document.getElementById('loginForm').addEventListener('submit', function (event){
    event.preventDefault()

    const email = document.getElementById("email").value
    const password = document.getElementById("password").value

    fetch('/auth', {
        method: 'POST',
        body: JSON.stringify({
            email,
            password
        }),
        headers: {
            'Content-Type': 'application/json'
        }}).then(response => {
        if (response.ok) {
            alert('Usuário logado!');
        } else {
            alert('Usuario ou senha incorretos');
        }
    }).catch(error => {
        alert('Erro: ' + error);
    });
});