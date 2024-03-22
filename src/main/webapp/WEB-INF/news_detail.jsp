<%@ page import="step.learning.entity.News" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Objects" %>
<%@ page import="step.learning.entity.User" %>
<%@ page import="step.learning.entity.Comment" %>
<%@ page import="java.util.Date" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String contextPath = request.getContextPath();
    Comment[] comments = (Comment[]) request.getAttribute("news_comments");
    News news = (News) request.getAttribute("news_detail");
    User user = (User) request.getAttribute("auth-user");
    List<News> allNews = (List<News>) request.getAttribute("news");
    String title = news.getTitle() == null
            ? "This news is deleted or does not exist"
            : news.getTitle();
    String spoiler = news.getSpoiler() == null
            ? "This spoiler is broken"
            : news.getSpoiler();
    String text = news.getText() == null
            ? "This text is broken"
            : news.getText();

    boolean canUpdate = Objects.equals(true, request.getAttribute("can-update"));/*(boolean) request.getAttribute("can-update");*/
    SimpleDateFormat dateFormatter = new SimpleDateFormat("HH:mm dd.MM.yyyy");
    SimpleDateFormat timeFormatter = new SimpleDateFormat("HH:mm");
%>
<link rel="stylesheet" href="<%=contextPath%>/css/site.css"/>

<% if (canUpdate) {%>
    <button class="btn-floating btn-large waves-effect waves-light red accent-2"
    onclick="newsEditClick()" style="position: fixed; right: 5px; top: 15vh"><i class="material-icons">edit</i></button>
<% } %>
<h4 data-editable="true" data-parameter="title"><%=title%>
</h4>
<br>
<% if (canUpdate) { %>
<h5 style="display: none" data-editable="true" data-parameter="spoiler"><%=spoiler%>
</h5>
<% } %>
<br>
<br>
<img src="<%= news.getImageUrl() != null && news.getImageUrl() != null ? contextPath + "/upload/news/" + news.getImageUrl() : contextPath + "/upload/avatar/NoImage.png" %>"
     alt="NewsImage" class="newsImage">
<h5 data-editable="true" data-parameter="text" data-news-edit-id="<%=news.getId()%>"><%=text%>
</h5>
<br>

<% for(Comment comment : comments ) { %>
<div class="comment">
    <div class="date">
        <%
            Date date = comment.getCreateDt();
            String strDate;
            if (dateFormatter.format(date).substring(6, 16).equals(dateFormatter.format(new Date()).substring(6, 16))) {
                strDate = timeFormatter.format(date);
            } else {
                strDate = dateFormatter.format(date);
            }
        %>
        <%= strDate %>
    </div>
    <div class="user-info">
        <img src="<%= comment.getUser() != null && comment.getUser().getFilename() != null ? contextPath + "/upload/avatar/" + comment.getUser().getFilename() : contextPath + "/upload/avatar/NoImage.png" %>"
             alt="User Avatar" class="avatar">
        <h5><%= comment.getUser() == null ? "--" : comment.getUser().getName() %></h5>
    </div>
    <div class="comment-text">
        <p><%= comment.getText() %></p>
    </div>
</div>
<% } %>

<% if (user != null) {%>
    <div class="row">
        <label for="news-comment-text">Comment</label>
        <input placeholder="Comment text" id="news-comment-text" type="text">
    </div>
    <div class="row">
        <button id="news-comment-button" class="btn red accent-2">Post</button>
    </div>
<input type="hidden" id="news-comment-user-id" value="<%=user.getId()%>">
<% } %>

<marquee direction="left" scrollamount="5">
    <%
        int count = 0;
        for (News sN : allNews) {
            if (news.getId() != sN.getId() && count < 3) {
    %>
    <span>|<a href="<%=contextPath%>/news/<%=sN.getId() %>"><%=sN.getSpoiler()%></a>|</span>
    <%
                count++;
            }
        }
    %>
</marquee>
