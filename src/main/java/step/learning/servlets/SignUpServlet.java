package step.learning.servlets;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.apache.commons.fileupload.FileItem;
import step.learning.services.form_parse.FormParseResult;
import step.learning.services.form_parse.FormParseService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static java.util.UUID.randomUUID;

@Singleton
public class SignUpServlet extends HttpServlet {

    private final FormParseService formParseService;

    @Inject
    public SignUpServlet(FormParseService formParseService) {
        this.formParseService = formParseService;
    }

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
        // API - JSON
        // Web - Redirect
        Map<String, String> errorMessages = new HashMap<>();
        FormParseResult formParseResult;
        HttpSession session = req.getSession();
        try {
            formParseResult = formParseService.parse(req);
        } catch (ParseException ex) {
            errorMessages.put("parse", ex.getMessage());
            session.setAttribute("form-status", errorMessages);
            resp.sendRedirect(req.getRequestURI());
            return;
        }
        Map<String, String> fields = formParseResult.getFields();

        String userName = fields.get("user-name");
        if (userName == null || userName.isEmpty()) {
            errorMessages.put("user-name", "Имя не может быть пустым");
        } else if (!userName.matches("[A-Z][a-z]*")) {
            errorMessages.put("user-name", "Имя должно начинатся с большой буквы");
        }

        String userPhone = fields.get("user-phone");
        if (userPhone == null || userPhone.isEmpty()) {
            errorMessages.put("user-phone", "Не може бути порожнім phone");
        }

        String userPassword = fields.get("user-password");
        if (userPassword == null || userPassword.isEmpty()) {
            errorMessages.put("user-password", "Не може бути порожнім password");
        }

        String userEmail = fields.get("user-email");
        if (userEmail == null || userEmail.isEmpty()) {
            errorMessages.put("user-email", "Не може бути порожнім email");
        }

        if (formParseResult.getFiles().containsKey("user-avatar")) {
            FileItem fileItem = formParseResult.getFiles().get("user-avatar");
            String fileName = fileItem.getName();
            int dotPosition = fileName.lastIndexOf(".");
            if (dotPosition == -1) {
                errorMessages.put("user-avatar", "Файлы без разсширения не допускаются");
            } else {
                String ext = fileName.substring(dotPosition);
//                System.out.println(ext);

                String savedFilename;
                File savedFile;
                do {
                    savedFilename = UUID.randomUUID() + ext;
                    savedFile = new File(
                            req.getServletContext().getRealPath("/upload/avatar"),
                            savedFilename
                    );
                } while (savedFile.isFile());

                try {
                    fileItem.write(savedFile);
                }
                catch (Exception e) {
                    throw new IOException(e);
                }
            }
        }

        session.setAttribute("form-status", errorMessages);
        resp.sendRedirect(req.getRequestURI());
    }
}