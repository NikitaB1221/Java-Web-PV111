package step.learning.servlets;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.inject.Singleton;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

@Singleton
public class CommentServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String reqBody = readStreamToEnd(req.getInputStream());
        sendRest(resp, "TEST", reqBody);
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
