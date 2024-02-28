document.addEventListener('DOMContentLoaded', function () {
    var elems = document.querySelectorAll('.modal');
    M.Modal.init(elems, {
        onCloseEnd: onAuthModalClosed
    });
    const authButton = document.getElementById("auth-button");
    if (authButton) authButton.addEventListener('click', authButtonClick);

});

function onAuthModalClosed() {
    const [authEmailInput, authPasswordInput, authMessage] = getAuthElements();
    authEmailInput.innerText = "";
    authPasswordInput.innerText = "";
    authMessage.innerText = "";
}

function getAuthElements() {
    const authEmailInput = document.getElementById("auth-email");
    if (!authEmailInput) throw "Element '#auth-email' not found";
    const authPasswordInput = document.getElementById("auth-password");
    if (!authPasswordInput) throw "Element '#auth-password' not found";
    const authMessage = document.querySelector(".auth-message");
    if (!authMessage) throw "Element '.auth-message' not found";
    return [authEmailInput, authPasswordInput, authMessage]
}

function authButtonClick() {

    const [authEmailInput, authPasswordInput, authMessage] = getAuthElements();

    const email = authEmailInput.value;
    if (email === "") {
        authMessage.innerText = "Необходимо заполнить поле 'Email'";
        return;
    }
    const password = authPasswordInput.value;
    if (password === "") {
        authMessage.innerText = "Необходимо заполнить поле 'Password'";
        return;
    }
    const appContext = window.location.pathname.split('/')[1];
    fetch(`/${appContext}/auth?email=${email}&password=${password}`)
        .then(r => r.json())
        .then(j => {
            if (j.status !== "success") {
                authMessage.innerText = "Аутентификация отклоненна!";
            } else {
                window.location.reload();
            }
        });
}

