<%@ page contentType="text/html;charset=UTF-8" %>
<%
    String pageBody = (String) request.getAttribute( "page-body" ) ;
    if( pageBody == null ) {
        pageBody = "home.jsp" ;   // default page
    }
%>
<!doctype html>
<html>
<head>
    <title>Java web</title>
</head>
<body>
<jsp:include page="<%= pageBody %>" />
</body>
</html>