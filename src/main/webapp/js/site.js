document.addEventListener('DOMContentLoaded', function () {
    let elems = document.querySelectorAll('.modal');
    M.Modal.init(elems, {
        onCloseEnd: onAuthModalClosed
    });
    const authButton = document.getElementById("auth-button");
    if (authButton) authButton.addEventListener('click', authButtonClick);

    const newsSubmitButton = document.getElementById("news-submit");
    if (newsSubmitButton) newsSubmitButton.addEventListener('click', newsSubmitClick);

    Date.prototype.toSqlString = function () {
        return `${this.getFullYear()}-${this.getMonth().toString().padStart(2, '0')}-${this.getDate().toString().padStart(2, '0')}`;
    }

    const newsImgFileInput = document.getElementById("news-file");
    if (newsImgFileInput) newsImgFileInput.onchange = newsImgChange;

    for (let a of document.querySelectorAll("[data-news-id]")){
        a.addEventListener('click', deleteNewsClick);
    }
});

function newsImgChange(e) {
    const [file] = e.target.files;
    if (file) {
        document.getElementById("news-image-preview").src = URL.createObjectURL(file);
    } else {
        const appContext = window.location.pathname.split('/')[1];
        document.getElementById("news-image-preview").src = "/" +
            appContext + '/upload/news/no-image.jpg';
    }
}

function deleteNewsClick(e){
    const newsId = e.target.closest("[data-news-id]").getAttribute("data-news-id")
    if (!newsId){
        alert("Empty news id? Call moder!");
        return;
    }
    const appContext = window.location.pathname.split('/')[1];
    let url = `/${appContext}/news/?id=${newsId}`;
    console.log(url);
    fetch(url, {
        method: 'DELETE'
    }).then(r => r.json()).then(console.log);
// console.log(newsId);
}
function newsSubmitClick() { // hoisting
    const newsTitle = document.getElementById("news-title");
    if (!newsTitle) throw "Element #news-title not found"
    const newsDate = document.getElementById("news-date");
    if (!newsDate) throw "Element #news-title not found"
    const newsSpoiler = document.getElementById("news-spoiler");
    if (!newsSpoiler) throw "Element #news-title not found"
    const newsText = document.getElementById("news-text");
    if (!newsText) throw "Element #news-title not found"
    const newsFile = document.getElementById("news-file");
    if (!newsFile) throw "Element #news-title not found"
    const authorId = document.getElementById("author-id");
    if (!newsFile) throw "Element #author-id not found"

    let isFormValid = true;

    const title = newsTitle.value.trim();
    if (title.length === 0) {
        newsTitle.classList.add("invalid");
        isFormValid = false;
    } else {
        newsTitle.classList.remove("invalid");
    }
    const spoiler = newsSpoiler.value.trim();
    if (spoiler.length === 0) {
        newsSpoiler.classList.add("invalid");
        isFormValid = false;
    } else {
        newsSpoiler.classList.remove("invalid");
    }
    const text = newsText.value.trim();
    if (text.length === 0) {
        newsText.classList.add("invalid");
        isFormValid = false;
    } else {
        newsText.classList.remove("invalid");
    }
    if (newsFile.files.length === 0) {
        document.getElementById("news-file-path").classList.add("invalid");
        isFormValid = false;
    }

    if (!newsDate.value) {
        newsDate.value = new Date().toSqlString();
    }

    if (isFormValid) {
        const formData = new FormData();
        formData.append("news-title", title);
        formData.append("news-date", newsDate.value);
        formData.append("news-spoiler", spoiler);
        formData.append("news-text", text);
        formData.append("news-file", newsFile.files[0]);
        formData.append("author-id", authorId.value.trim());

        const appContext = window.location.pathname.split('/')[1];
        fetch(`/${appContext}/news/`, {
            method: 'POST',
            body: formData
        }).then(r => r.json()).then(j => {
            if (j.status === "success") {
                console.log('OK')
            } else {
                console.log('NO')
            }
        });
    }

}

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

