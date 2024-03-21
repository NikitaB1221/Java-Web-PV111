package step.learning.servlets;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.apache.commons.fileupload.FileItem;
import step.learning.dal.CommentDao;
import step.learning.dal.NewsDao;
import step.learning.dal.UserDao;
import step.learning.entity.Comment;
import step.learning.entity.News;
import step.learning.entity.User;
import step.learning.services.form_parse.FormParseResult;
import step.learning.services.form_parse.FormParseService;

import javax.servlet.ServletException;
import javax.servlet.http.*;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

@Singleton
public class NewsServlet extends HttpServlet {
    private final UserDao userDao;
    private final NewsDao newsDao;
    private final CommentDao commentDao;
    private final FormParseService formParseService;
    private final Logger logger;

    @Inject
    public NewsServlet(UserDao userDao, NewsDao newsDao, UserDao userDao1, CommentDao commentDao, FormParseService formParseService, Logger logger) {
        this.userDao = userDao1;
        this.newsDao = newsDao;
        this.commentDao = commentDao;
        this.formParseService = formParseService;
        this.logger = logger;
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String newsId = req.getParameter("id");
        if (newsId == null || newsId.isEmpty()) {
            sendRest(resp, "error", "Missing or invalid data: 'newsId'. Must not null");
            return;
        }
        if(newsDao.deleteNews (newsId)) {
            sendRest (resp, "success", "News deleted");
        }
        else {
            sendRest (resp, "error", "Internal error.");
        }
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        switch (req.getMethod()){
            case "RESTORE": doRestore(req, resp);
                break;
            default: super.service(req,resp);
        }
    }

    protected void doRestore(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String newsId = req.getParameter("id");
        if (newsId == null || newsId.isEmpty()) {
            sendRest(resp, "error", "Missing or invalid data: 'newsId'. Must not null");
            return;
        }
        if(newsDao.restoreNews (newsId)) {
            sendRest (resp, "success", "News deleted");
        }
        else {
            sendRest (resp, "error", "Internal error.");
        }
    }
    boolean canDelete = false;
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setAttribute("create-status",
                newsDao.installTable()
                        ? "Success"
                        : "Error");
        User user = (User) req.getAttribute("auth-user");
        if(user != null) {
            req.setAttribute("can-create",
                    user.getRoles().stream().anyMatch (role -> role.getCanCreate() == 1));
            req.setAttribute("can-update",
                    user.getRoles().stream().anyMatch(role -> role.getCanUpdate() == 1));
            canDelete = user.getRoles().stream().anyMatch(role -> role.getCanDelete() == 1);
            req.setAttribute("can-delete", canDelete);
        }
        String pathInfo = req.getPathInfo();
        if ("/".equals(pathInfo)) {
            req.setAttribute("news", newsDao.getAll(canDelete));
            req.setAttribute("userDaoObj", userDao);
            req.setAttribute("page-body", "news.jsp");
        } else {
            News news = newsDao.getById(pathInfo.substring(1));
            if (news != null) {
                req.setAttribute("news_detail", news);
                req.setAttribute("news_comments", commentDao.getNewsComment(news).toArray(new Comment[0]));
            }
            req.setAttribute("news", newsDao.getAll(canDelete));
            req.setAttribute("userDaoObj", userDao);
            req.setAttribute("page-body", "news_detail.jsp");
        }

        req.getRequestDispatcher("../WEB-INF/_layout.jsp")  // перенаправлення (внутрішнє)
                .forward(req, resp);
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        FormParseResult formParseResult;
        try {
            formParseResult = formParseService.parse(req);
        }
        catch (ParseException ex) {
            logger.log(Level.SEVERE, ex.getMessage());
            sendRest(resp, "error", "Data composition error");
            return;
        }
        Map<String, String> fields = formParseResult.getFields();

        if (fields.size() <= 1 && fields.containsKey("id")) {
            sendRest(resp, "error", "At least one field other than ID must be provided for update.");
            return;
        }

        sendRest(resp, String.join(",", fields.keySet()), String.join(",", fields.values()));
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        FormParseResult formParseResult;
        try {
            formParseResult = formParseService.parse(req);
        } catch (ParseException ex) {
            logger.log(Level.SEVERE, ex.getMessage());
            sendRest(resp, "error", "Data composition error");
            return;
        }
        Map<String, String> fields = formParseResult.getFields();
        String newsTitle = fields.get("news-title");
        if (newsTitle == null || newsTitle.isEmpty() || newsTitle.length() < 10) {
            sendRest(resp, "error", "Missing or invalid data: 'news-title'. Must be at least 10 characters long");
            return;
        }
        String newsDate = fields.get("news-date");
        if (newsDate == null || newsDate.isEmpty() || !newsDate.matches("\\d{2}-\\d{2}-\\d{4}")) {
            sendRest(resp, "error", "Missing or invalid data: 'news-date'. Must be in the format YYYY-MM-DD");
            return;
        }
        String newsSpoiler = fields.get("news-spoiler");
        if (newsSpoiler == null || newsSpoiler.isEmpty() || newsSpoiler.split(" ").length < 10) {
            sendRest(resp, "error", "Missing or invalid data: 'news-spoiler'. Must be at least 10 words long");
            return;
        }
        String newsText = fields.get("news-text");
        if (newsText == null || newsText.isEmpty() || newsText.length() < 300) {
            sendRest(resp, "error", "Missing or invalid data: 'news-text'. Must be at least 300 characters long");
            return;
        }
        if (!formParseResult.getFiles().containsKey("news-file")) {
            sendRest(resp, "error", "Missing required data: 'news-file'");
            return;
        }
        FileItem fileItem = formParseResult.getFiles().get("news-file");
        String savedFilename = null;
        String fileName = fileItem.getName();
        int dotPosition = fileName.lastIndexOf(".");
        if (dotPosition == -1 || !fileName.substring(dotPosition).matches("\\.(jpg|png|gif|bmp)$")) {
            sendRest(resp, "error", "File must have a graphic extension (jpg, png, gif, bmp): 'news-file'");
            return;
        }
        String ext = fileName.substring(dotPosition);
        File savedFile;
        do {
            savedFilename = UUID.randomUUID() + ext;
            savedFile = new File(req.getServletContext().getRealPath("/upload/news"), savedFilename);
        } while (savedFile.isFile());

        try {
            fileItem.write(savedFile);
        } catch (Exception ex) {
            logger.log(Level.SEVERE, ex.getMessage());
            sendRest(resp, "error", "File transfer error: 'news-file'");
            return;
        }
        String authorId = fields.get("author-id");
        if (authorId == null) {
            sendRest(resp, "error", "Missing or invalid data: 'author-id'. Error during extracting your id");
            return;
        }

        News news = new News();
        news.setTitle(newsTitle);
        news.setSpoiler(newsSpoiler);

        try {
            news.setCreateDt(new SimpleDateFormat("yyyy-MM-dd").parse(newsDate));
        } catch (ParseException e) {
            sendRest(resp, "error", "Invalid data format: 'news-date'. YYYY-MM-DD expected");
            return;
        }
        news.setText(newsText);
        news.setImageUrl(savedFilename);
        news.setAuthor_id(UUID.fromString(authorId));

        if (newsDao.addNews(news)) {
            sendRest(resp, "success", "News created");
        } else {
            sendRest(resp, "error", "Internal error.");
        }
    }

    private void sendRest(HttpServletResponse resp, String status, String message) throws IOException {
        Gson gson = new Gson();
        JsonObject res = new JsonObject();
        res.addProperty("status", status);
        res.addProperty("message", message);
        resp.getWriter().print(gson.toJson(res));
    }
}

