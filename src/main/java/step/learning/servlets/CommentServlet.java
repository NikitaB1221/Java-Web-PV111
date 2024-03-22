package step.learning.servlets;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import step.learning.dal.CommentDao;
import step.learning.entity.Comment;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

@Singleton
public class CommentServlet extends HttpServlet {
    private final CommentDao commentDao;

    @Inject
    public CommentServlet(CommentDao commentDao) {
        this.commentDao = commentDao;
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String reqBody = readStreamToEnd( req.getInputStream() ) ;
        JsonObject jsonBody = JsonParser.parseString(reqBody).getAsJsonObject();
        // newsId, userId, comment
        if( ! jsonBody.has("newsId") ) {
            sendRest(resp, "error", "Missing required data: 'newsId'");
            return;
        }
        String newsId = jsonBody.get("newsId").getAsString();  // TODO:перевірити на формат UUID

        if( ! jsonBody.has("userId") ) {
            sendRest(resp, "error", "Missing required data: 'userId'");
            return;
        }
        String userId = jsonBody.get("userId").getAsString();

        if( ! jsonBody.has("comment") ) {
            sendRest(resp, "error", "Missing required data: 'comment'");
            return;
        }
        String commentText = jsonBody.get("comment").getAsString();
        Comment comment = new Comment();
        comment.setNewsId( UUID.fromString(newsId));
        comment.setUserId(UUID.fromString(userId));
        comment.setText(commentText);
        if(commentDao.addComment(comment) ) {
            sendRest(resp, "success", "Comment added");
        }
        else {
            sendRest (resp, "error", "Internal error.");
        }
    }

    private String readStreamToEnd(InputStream inputStream) throws IOException {

        final byte[] buffer = new byte[32 * 1024];  // 32k
        ByteArrayOutputStream byteBuilder = new ByteArrayOutputStream();
        int len;
        while ((len = inputStream.read(buffer)) > -1) {
            byteBuilder.write(buffer, 0, len);
        }
        return byteBuilder.toString(StandardCharsets.UTF_8.name());
    }

    private void sendRest(HttpServletResponse resp, String status, String message) throws IOException {
        Gson gson = new Gson();
        JsonObject res = new JsonObject();
        res.addProperty("status", status);
        res.addProperty("message", message);
        resp.getWriter().print(gson.toJson(res));
    }
}
