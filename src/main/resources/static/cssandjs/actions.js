function shake(element) {
    element.classList.add('shake');

    setTimeout(() => {
        element.classList.remove('shake');
    }, 250);
}

