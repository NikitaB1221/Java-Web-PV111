<%--
  Created by IntelliJ IDEA.
  User: nikit
  Date: 01.03.2024
  Time: 18:05
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String contextPath = request.getContextPath();
%>
<html>
<head>
    <title>Page works in static mode</title>
    <link rel="stylesheet" href="<%=contextPath%>/css/site.css">
    <link rel="stylesheet" href="<%=contextPath%>/js/site.js">
    <h2>No connection to DB</h2>
    <img src="<%=contextPath%>/img/database-error.jpg" alt="ErrorImg.png">
</head>
<body>

</body>
</html>
