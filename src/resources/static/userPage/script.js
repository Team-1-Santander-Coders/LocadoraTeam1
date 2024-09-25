document.addEventListener('DOMContentLoaded', () => {
    const user = JSON.parse(localStorage.getItem('user'))

    if (!user) {
        document.body.innerHTML = '';
        window.location.href = '/';
    }
    document.getElementById("welcome_message").textContent =`Bem vindo, ${user.name.split(' ')[0]}!`
});



