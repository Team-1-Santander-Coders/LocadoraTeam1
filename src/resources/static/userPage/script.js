const user = JSON.parse(localStorage.getItem('user'))

document.getElementById("welcome_message").textContent =`Bem vindo, ${user.name.split(' ')[0]}!`