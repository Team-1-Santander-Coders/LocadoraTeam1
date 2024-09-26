function deleteCookie(name) {
    const paths = ['/', '/userPage', '/adminPage'];
    const domain = 'localhost';
    const pastDate = new Date(0).toUTCString();

    paths.forEach(path => {
        document.cookie = `${name}=; expires=${pastDate}; path=${path}; domain=${domain}`;
    });

    console.log(`Cookie '${name}' foi deletado em todos os paths.`);
}

function getCookie(name) {
    const value = `; ${document.cookie}`;
    const parts = value.split(`; ${name}=`);
    if (parts.length === 2) return parts.pop().split(';').shift();
}

function capitalizeFirstLetters(text) {
    return text
        .toLowerCase()
        .replace(/\b\w/g, char => char.toUpperCase());
}

document.addEventListener('DOMContentLoaded', () => {
    const userId = getCookie('userId');
    const userMenu = document.getElementById('user_menu');
    let page = window.location.href;

    if (userId) {
        fetch('/checkAuth', {
            method: 'post',
            credentials: 'include',
        })
            .then(response => response.json())
            .then(data => {
                if (data) {
                    const nome = data.name;
                    const isAdmin = data.isAdmin;
                    const loginMenu = document.getElementById("login-menu");

                    if (page === "http://localhost:8000/" || page === "http://localhost:8000") {
                        userMenu.innerHTML = `Olá, ${capitalizeFirstLetters(nome)}, acesse seu <a href="${isAdmin ? '/adminPage' : '/userPage'}">Painel do ${isAdmin ? 'administrador' : 'usuário'}</a> ou aqui para <a id="logout" href="javascript:void(0)">sair</a>`;
                        if (loginMenu) {
                            loginMenu.remove();
                        }

                        document.getElementById("logout").addEventListener("click", function (event) {
                            event.preventDefault();
                            console.log("teste")
                            deleteCookie('userId');
                            window.location.href = "/";
                        });
                    }

                    if (page.toLowerCase().includes("userpage") || page.toLowerCase().includes("adminpage")) {
                        userMenu.innerHTML = `Olá, ${capitalizeFirstLetters(nome)}! Para sair clique aqui: <a id="logout" href="javascript:void(0)">Sair</a>`;
                        if (loginMenu) {
                            loginMenu.remove();
                        }

                        document.getElementById("logout").addEventListener("click", function (event) {
                            event.preventDefault();
                            console.log("teste")
                            deleteCookie('userId');
                            window.location.href = "/";
                        });
                    }


                }

                if (data.isAdmin && page.includes('userPage')) {
                    window.location.href = "/adminPage";
                }
                if (!data.isAdmin && page.includes('adminPage')) {
                    window.location.href = "/userPage";
                }
            })
            .catch(error => {
                console.error(error);
            });
    } else {
        if (!(page === "http://localhost:8000/" || page === "http://localhost:8000")) {
            window.location.href = "/";
        }
    }
});

try {
    document.getElementById('loginForm').addEventListener('submit', function (event) {
        event.preventDefault();
        deleteCookie("userId")
        const email = document.getElementById("email").value;
        const password = document.getElementById("password").value;

        fetch('/auth', {
            method: 'POST',
            body: JSON.stringify({
                email,
                password
            }),
            headers: {
                'Content-Type': 'application/json'
            }
        })
            .then(response => response.json())
            .then(data => {
                if (data.success) {
                    window.location.href = data.redirectUrl;
                } else {
                    console.error('Erro:', data);
                }
            })
            .catch(error => {
                console.error('Erro:', error);
                alert("Usuário ou senha incorretos.");
            })
    });
} catch (e) {
}


