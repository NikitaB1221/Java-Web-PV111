<%@ page import="step.learning.entity.User" %>
<%@ page import="step.learning.entity.Role" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<%
    String contextPath = request.getContextPath();
    User user = (User) request.getAttribute("profile-user");
%>
<% if (user == null) { %>
<h1>Requested profile not found</h1>
<% } else { %>
    <h1> <%=user.getName()%>'s profile </h1>
        <img src="<%= user.getFilename() != null ? contextPath + "/upload/avatar/" + user.getFilename() : contextPath + "/upload/avatar/NoImage.png" %>" alt="User Avatar">
    <% for (Role role : user.getRoles()) { %>
        <p>Has role: <%=role.getName()%></p>
    <% } %>
<% } %>