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
        }}).then(response => response.json())
        .then(user => {
            localStorage.setItem('user', JSON.stringify(user));
            console.log(user.name + " logado com sucesso!");
            window.location.href = "userPage/index.html"
        })
        .catch(error => console.error('Erro:', error));
});