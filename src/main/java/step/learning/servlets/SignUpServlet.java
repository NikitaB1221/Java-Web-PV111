package step.learning.servlets;

import com.google.inject.Singleton;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Singleton
public class SignUpServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        Map<String, String> errorMessages = (Map<String, String>) session.getAttribute("form-status");
        if (errorMessages != null) {
            req.setAttribute("errorMessages", errorMessages);
            session.removeAttribute("form-status");
        }
        req.setAttribute("page-body", "signup.jsp");
        req.getRequestDispatcher("WEB-INF/_layout.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Map<String, String> errorMessages = new HashMap<>();

        String userName = req.getParameter("user-name");

        if (userName == null || userName.isEmpty()) {
            errorMessages.put("user-name", "Не може бути порожнім");
        } else if (!userName.matches("[A-Z][a-z]*")) {
            errorMessages.put("user-name", "Ім'я повинно починатися з великої літери, а решта маленькі");
        }

        HttpSession session = req.getSession();

        if (!errorMessages.isEmpty()) {
            session.setAttribute("form-status", errorMessages);
            resp.sendRedirect(req.getRequestURI());
        } else {
            resp.sendRedirect("success.jsp");
        }
    }
}