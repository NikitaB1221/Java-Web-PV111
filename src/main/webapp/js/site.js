document.addEventListener('DOMContentLoaded', function () {
    var elems = document.querySelectorAll('.modal');
    M.Modal.init(elems, {});
    const authButton = document.getElementById("auth-button");
    if (authButton) authButton.addEventListener('click', authButtonClick)
});
function authButtonClick(){
    const authEmailInput = document.getElementById( "auth-email");
    if( ! authEmailInput) throw "Element '#auth-email' not found";
    const
        authPasswordInput = document.getElementById( "auth-password");
    if( ! authPasswordInput) throw "Element '#auth-password' not found";
    const email = authEmailInput.value;
    if(email === "") {
        alert("Необходимо заполнить поле 'Email'");
        return;
    }
    const password = authPasswordInput.value;
    if (password === "") {
        alert("Необходимо заполнить поле 'Password'");
        return;
    }
    const appContext= window.location.pathname.split( '/')[1];
    fetch(`/${appContext}/auth?email=${email}&password=${password}`)
        .then(r => r.json()).then(console.log);
}
