function navigateWithTransition(url) {
    const overlay = document.getElementById('transition-overlay');
    overlay.classList.remove('fade-out')
    overlay.classList.add('fade-in')

    setTimeout(function() {
        window.location.href = url;
    }, 500);
}


window.addEventListener('load', function() {
    const overlay = document.getElementById('transition-overlay');
    overlay.classList.add('fade-out');


    setTimeout(function() {
        overlay.style.display = 'none'; // Esconde o overlay após a transição
    }, 500);
});


document.querySelectorAll('a').forEach(function(link) {
    link.addEventListener('click', function(event) {
        event.preventDefault();
        navigateWithTransition(link.href);
    });
});