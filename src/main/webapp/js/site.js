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

    for (let a of document.querySelectorAll("[data-news-id]")) {
        a.addEventListener('click', deleteNewsClick);
    }
    for (let a of document.querySelectorAll("[data-news-restore-id]")) {
        a.addEventListener('click', restoreNewsClick);
    }
    const newsCommentButton = document.getElementById("news-comment-button");

    function newsCommentClick() {
        const dataId = document.querySelector("[data-news-edit-id]");
        if (!dataId) throw "[data-news-edit-id] not found";
        const newsId = dataId.getAttribute("data-news-edit-id");
        if (!newsId) throw "New id attribute is empty";
        const commentInput = document.getElementById("news-comment-text");
        if (!commentInput) throw "#news-comment-text not found";
        const comment = commentInput.value.trim();
        if (comment.length <= 5) {
            alert("Коментар занадто короткий");
            return;
        }
        const userIdInput = document.getElementById("news-comment-user-id");
        if (!userIdInput) throw "#news-comment-user-id not found";
        const userId = userIdInput.value;
        const appContext = window.location.pathname.split('/')[1];
        fetch(`/${appContext}/comment`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                newsId, userId, comment
            })
        }).then(r => r.json()).then(r => r.json()).then(j => {
            console.log(j);
            if (j.status !== "success") {
                alert(" Ошибка сервера");
            } else {
                window.location.reload();
            }
        });
    }

    if (newsCommentButton) newsCommentButton.addEventListener('click', newsCommentClick);
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

function deleteNewsClick(e) {
    const newsId = e.target.closest("[data-news-id]").getAttribute("data-news-id")
    if (!newsId) {
        alert("Empty news id? Call moder!");
        return;
    }
    const appContext = window.location.pathname.split('/')[1];
    let url = `/${appContext}/news/?id=${newsId}`;
    console.log(url);
    fetch(url, {
        method: 'DELETE'
    }).then(r => r.json()).then(j => {
        if (j.status !== "success") {
            alert("Помилка сервера");
        } else {
            window.location.reload();
        }
    });
}

function restoreNewsClick(e) {
    const newsId = e.target.closest("[data-news-restore-id]").getAttribute("data-news-restore-id")
    if (!newsId) {
        alert("Empty news id? Call moder!");
        return;
    }
    const appContext = window.location.pathname.split('/')[1];
    let url = `/${appContext}/news/?id=${newsId}`;
    console.log(url);
    fetch(url, {
        method: 'RESTORE'
    }).then(r => r.json()).then(j => {
        if (j.status !== "success") {
            alert("Помилка сервера");
        } else {
            window.location.reload();
        }
    });
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

function newsEditClick() {
    const editables = document.querySelectorAll(`[data-editable="true"]`);
    if (editables.length === 0) {
        return;
    }
    const isEdit = editables[0].getAttribute("contenteditable");
    if (isEdit) {
        let formData = new FormData();
        for (let element of editables) {
            element.removeAttribute("contenteditable");
            if (element.getAttribute("initial-content") !== element.innerText) {
                console.log("Changes in " + element.getAttribute("data-parameter"));
                formData.append(element.getAttribute("data-parameter"), element.innerText);
            }
        }
        if ([...formData.keys()].length > 1 || (![...formData.keys()].includes("id") && [...formData.keys()].length > 0)) {
            const dataId = document.querySelector("[data-news-edit-id]");
            if (!dataId) throw "[data-news-edit-id] not found";
            const newsId = dataId.getAttribute("data-news-edit-id");
            if (!newsId) throw "New id attribute is empty"
            const appContext = window.location.pathname.split('/')[1];
            fetch(`/${appContext}/news/`, {
                method: 'PUT',
                body: formData
            }).then(r => r.json()).then(j => {
                console.log(j);
                if (j.status !== "success") {
                    alert("Помилка сервера");
                } else {
                    window.location.reload();
                }
            });
        } else {
            alert("At least one field other than ID must be provided for update.");
        }
    } else {
        for (let element of editables) {
            element.setAttribute("contenteditable", true);
            element.setAttribute("initial-content", element.innerText);
        }
    }
}

