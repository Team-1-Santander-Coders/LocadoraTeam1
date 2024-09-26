try {
    document.addEventListener('DOMContentLoaded', () => {
        if (!user) {
            document.body.innerHTML = '';
            window.location.href = '/';
        }
        document.getElementById("welcome_message").textContent =`Bem vindo, ${user.name.split(' ')[0]}!`
    });


    document.addEventListener('DOMContentLoaded', () => {
        const user = JSON.parse(localStorage.getItem('user'))

        if (!user) {
            document.body.innerHTML = '';
            window.location.href = '/';
        }
        document.getElementById("welcome_message").textContent =`Bem vindo, ${user.name.split(' ')[0]}!`
    });

    document.getElementById("welcome_message").textContent =`Bem vindo, ${user.name.split(' ')[0]}!`
} catch (e) {
    console.error(e);
}

