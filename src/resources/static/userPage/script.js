const user = JSON.parse(localStorage.getItem('user'))

document.addEventListener('DOMContentLoaded', () => {
    const user = JSON.parse(localStorage.getItem('user')) // Substitua 'chaveDoToken' pela chave que deseja verificar

    if (!user) {
        document.body.innerHTML = '';
        window.location.href = '/';
    }
    document.getElementById("welcome_message").textContent =`Bem vindo, ${user.name.split(' ')[0]}!`
});

document.getElementById("welcome_message").textContent =`Bem vindo, ${user.name.split(' ')[0]}!`

document.getElementById("exit").addEventListener("click", ()=>{
    localStorage.clear()

})