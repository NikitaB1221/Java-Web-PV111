<%@ page contentType="text/html;charset=UTF-8" %>
<%
    String pageBody = (String) request.getAttribute("page-body");
    if (pageBody == null) {
        pageBody = "home.jsp";   // default page
    }
    String contextPath = request.getContextPath();
%>
<!doctype html>
<html>
<head>
    <!--Import Google Icon Font-->
    <link rel="stylesheet" href="https://fonts.googleapis.com/icon?family=Material+Icons">
    <!-- Compiled and minified CSS -->
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/materialize/1.0.0/css/materialize.min.css">
    <link rel="stylesheet" href="<%=contextPath%>/css/site.css"/>
    <title>Java web</title>
</head>
<body style="display: flex; flex-direction: column; justify-content: space-between; min-height: 100vh">
<nav>
    <div class="nav-wrapper  ">
        <a href="<%=contextPath%>" class="brand-logo left">Logo</a>
        <ul id="nav-mobile" class="right ">
            <li><a href="<%=contextPath%>/ioc"><i class="material-icons">sync</i>IoC</a></li>
            <li><a href="<%=contextPath%>/privacy">Privacy</a></li>
            <li><a href="<%=contextPath%>/signup"><i class="material-icons">person_add</i></a></li>
        </ul>
    </div>
</nav>
<div class="container">
    <jsp:include page="<%= pageBody %>"/>
</div>
<div style="flex: 1 1 auto"></div>
<footer class="page-footer style='width: 100%'">
    <div class="container">
        <div class="row">
            <div class="col l6 s12">
                <h5 class="white-text">Footer Content</h5>
                <p class="grey-text text-lighten-4">You can use rows and columns here to organize your footer
                    content.</p>
            </div>
            <div class="col l4 offset-l2 s12">
                <h5 class="white-text">Links</h5>
                <ul>
                    <li><a class="grey-text text-lighten-3" href="#!">Link 1</a></li>
                    <li><a class="grey-text text-lighten-3" href="#!">Link 2</a></li>
                    <li><a class="grey-text text-lighten-3" href="#!">Link 3</a></li>
                    <li><a class="grey-text text-lighten-3" href="#!">Link 4</a></li>
                </ul>
            </div>
        </div>
    </div>
    <div class="footer-copyright">
        <div class="container">
            © 2024 Copyright Text
            <a class="grey-text text-lighten-4 right" href="#!">More Links</a>
        </div>
    </div>
</footer>
<!-- Modal Trigger --> <a class="waves-effect red lighten-2 btn modal-trigger" href="#modal1">Modal</a>
<!-- Modal Structure -->
<div id="modal1" class="modal">
    <div class="modal-content"><h4>Modal Header</h4>
        <p>A bunch of text</p></div>
    <div class="modal-footer"><a href="#!" class="modal-close waves-effect deep-orange accent-2 btn-flat">Agree</a></div>
</div>
<!-- Compiled and minified JavaScript -->
<script src="https://cdnjs.cloudflare.com/ajax/libs/materialize/1.0.0/js/materialize.min.js"></script>
<script>
    document.addEventListener('DOMContentLoaded', function () {
        var elems = document.querySelectorAll('.modal');
        M.Modal.init(elems, {});
    });
</script>
</body>
</html>