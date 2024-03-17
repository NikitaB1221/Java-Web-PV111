<%@ page import="step.learning.entity.News" %>
<%@ page import="java.util.List" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String contextPath = request.getContextPath();
    News news = (News) request.getAttribute("news_detail");
    List<News> allNews = (List<News>) request.getAttribute("news");
    String title = news == null
            ? "This news is deleted or does not exist"
            : news.getTitle();
    String spoiler = news == null
            ? "This spoiler is broken"
            : news.getSpoiler();
    String text = news == null
            ? "This text is broken"
            : news.getText();
%>
<h4><%=title%></h4>
<br>
<h5><%=spoiler%></h5>
<br>
<br>
<img src="<%= news.getImageUrl() != null && news.getImageUrl() != null ? contextPath + "/upload/news/" + news.getImageUrl() : contextPath + "/upload/avatar/NoImage.png" %>"
     alt="NewsImage" class="Image">
<h5><%=text%></h5>
<br>
<marquee direction="left" scrollamount="5">
<%
    int count = 0;
    for( News sN : allNews) {
        if (news.getId() != sN.getId() && count < 3){
%>
    <span>|<a href="<%=contextPath%>/news/<%=sN.getId() %>"><%=sN.getSpoiler()%></a>|</span>
<%
            count++;
        }
    }
%>
</marquee>
