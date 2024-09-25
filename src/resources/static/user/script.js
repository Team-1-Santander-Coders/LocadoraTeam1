(function(){
    emailjs.init("-Z51VkHqRSKon-Gi5");
})();

document.getElementById('sendCodeButton').addEventListener('click', function(event) {
    event.preventDefault();

    const email = document.getElementById('email').value;
    const code = Math.floor(1000 + Math.random() * 9000);

    emailjs.send("service_h01md0f", "template_3tscxhn", {
        to_email: email,
        verification_code: code
    }).then(function(response) {
        alert("Código enviado com sucesso!");
        localStorage.setItem('verificationCode', code);
        document.getElementById('verificationSection').style.display = 'block';
    }).catch(function(error) {
        alert("Erro ao enviar código: " + error);
    });
});

document.getElementById('verifyCodeButton').addEventListener('click', function(event) {
    event.preventDefault();

    const verificationCode = document.getElementById('verificationCode').value;
    const storedCode = localStorage.getItem('verificationCode');

    if (verificationCode === storedCode) {
        alert('Código validado com sucesso!');
        document.getElementById('user-form-pos-validation').style.display = 'block';
        document.getElementById('verificationSection').style.display = 'none';
    } else {
        alert('Código de verificação inválido.');
    }
});

document.getElementById('userForm').addEventListener('submit', function(event) {
    event.preventDefault();

    const name = document.getElementById('name').value;
    const address = document.getElementById('address').value.replace(/[^a-z0-9]/gi, "").replace(/,/g, "-");
    const password = document.getElementById('password').value;
    const email = document.getElementById('email').value;
    const phone = document.getElementById('phone').value.replace(/[^0-9]/gi, '');
    const documentValue = document.getElementById('documentValue').value.replace(/[^0-9]/gi, '');
    const tipo = document.getElementById('type').value;

    if (!name || !address || !password || !email || !phone || !documentValue || !tipo) {
        alert("Preencha todos os campos!");
        return;
    }

    fetch('/user', {
        method: 'POST',
        body: JSON.stringify({
            name,
            address,
            password,
            email,
            phone,
            documentValue,
            tipo
        }),
        headers: {
            'Content-Type': 'application/json'
        }
    }).then(response => {
        if (response.ok) {
            alert('Usuário cadastrado com sucesso!');
            document.getElementById('userForm').reset();
            document.getElementById('user-form-pos-validation').style.display = 'none';
            document.getElementById('verificationSection').style.display = 'none';
        } else {
            alert('Erro ao cadastrar usuário.');
        }
    }).catch(error => {
        alert('Erro: ' + error);
    });
});
