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
    <link rel="stylesheet" href="<%=contextPath%>/css/site.css">

    <title>Java web</title>
</head>
<body>
<nav>
    <div class="nav-wrapper">
        <a href="#" class="brand-logo">Logo</a>
        <ul id="nav-mobile" class="right ">
            <li><a href="<%=contextPath%>/ioc"><i class="material-icons">sync</i>IoC</a></li>
            <li><a href="#">Components</a></li>
            <li><a href="#">JavaScript</a></li>
        </ul>
    </div>
</nav>
<jsp:include page="<%= pageBody %>"/>

<!-- Compiled and minified JavaScript -->
<script src="https://cdnjs.cloudflare.com/ajax/libs/materialize/1.0.0/js/materialize.min.js"></script>

</body>
<footer class="page-footer">
    <div class="container">
        <div class="row">
            <div class="col l6 s12">
                <h5 class="white-text">Footer Content</h5>
                <p class="grey-text text-lighten-4">You can use rows and columns here to organize your footer
                    content.</p></div>
            <div class="col l4 offset-l2 s12"><h5 class="white-text">Links</h5>
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
        <div class="container"> © 2014 Copyright Text <a class="grey-text text-lighten-4 right" href="#!">More Links</a>
        </div>
    </div>
</footer>
</html>