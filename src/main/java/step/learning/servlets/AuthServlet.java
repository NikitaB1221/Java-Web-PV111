package step.learning.servlets;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import step.learning.dal.UserDao;
import step.learning.entity.User;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Singleton
public class AuthServlet extends HttpServlet {
    private final UserDao userDao;

    @Inject
    public AuthServlet(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setCharacterEncoding("UTF-8");
        JsonObject res = new JsonObject();
        String email = req.getParameter("email");
        String password = req.getParameter("password");
        if (email == null) {
            res.addProperty("status", "error");
            res.addProperty("errorMessage", "Missing required data: 'email'");
        } else if (password == null) {
            res.addProperty("status", "error");
            res.addProperty("errorMessage", "Missing required data: 'password'");
        } else {
            User user = userDao.getUserByCredentials(email, password);
            if (user == null){
                res.addProperty("status", "error");
                res.addProperty("errorMessage", "Credentials rejected");
            }
            res.addProperty("status", "success");
            JsonObject data = new JsonObject();
            data.addProperty("email", email);
            data.addProperty("password", password);
            res.add("data", data);
        }
        resp.getWriter().print(
                new Gson().toJson(res)
        );
    }
}
