package step.learning.servlets;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.apache.commons.fileupload.FileItem;
import step.learning.dal.UserDao;
import step.learning.services.form_parse.FormParseResult;
import step.learning.services.form_parse.FormParseService;

import javax.servlet.ServletException;
import javax.servlet.http.*;
import java.io.*;
import java.text.ParseException;
import java.util.*;
import java.util.logging.*;

import static java.util.UUID.randomUUID;

@Singleton
public class SignUpServlet extends HttpServlet {

    private final FormParseService formParseService;
    private final UserDao userDao;
    private final Logger logger;

    @Inject
    public SignUpServlet(FormParseService formParseService, UserDao userDao, Logger logger) {
        this.formParseService = formParseService;
        this.userDao = userDao;
        this.logger = logger;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        logger.log(Level.WARNING,"SignUpServlet.doGet activity registered");
        HttpSession session = req.getSession();
        Map<String, String> errorMessages = (Map<String, String>) session.getAttribute("form-status");
        if (errorMessages != null) {
            req.setAttribute("errorMessages", errorMessages);
            session.removeAttribute("form-status");
        }
//        userDao.installTable();
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
        boolean isValid = true;
        String userName = fields.get("user-name");
        if (userName == null || userName.isEmpty()) {
            errorMessages.put("user-name", "Имя не может быть пустым");
            isValid = false;
        } else if (!userName.matches("[A-Z][a-z]*")) {
            errorMessages.put("user-name", "Имя должно начинатся с большой буквы");
            isValid = false;
        }

        String userPhone = fields.get("user-phone");
        if (userPhone == null || userPhone.isEmpty()) {
            errorMessages.put("user-phone", "Телефон не может быть пустым");
            isValid = false;
        }

        String userPassword = fields.get("user-password");
        if (userPassword == null || userPassword.isEmpty()) {
            errorMessages.put("user-password", "Пароль не может быть пустым");
            isValid = false;
        }

        String userEmail = fields.get("user-email");
        if (userEmail == null || userEmail.isEmpty()) {
            errorMessages.put("user-email", "Имейл не может быть пустым");
            isValid = false;
        }
        String savedFilename = null;

        if (formParseResult.getFiles().containsKey("user-avatar")) {
            FileItem fileItem = formParseResult.getFiles().get("user-avatar");
            String fileName = fileItem.getName();
            if (fileName != null && !fileName.isEmpty()) {
                int dotPosition = fileName.lastIndexOf(".");
                if (dotPosition == -1) {
                    errorMessages.put("user-avatar", "Файлы без розширения не допускаються");
                    isValid = false;
                } else {
                    String ext = fileName.substring(dotPosition);
                    List<String> allowedExtensions = Arrays.asList(".jpg", ".jpeg", ".png", ".gif");
                    if (!allowedExtensions.contains(ext.toLowerCase())) {
                        errorMessages.put("user-avatar", "Файл имеет недопустиме розширенние для изображения");
                        isValid = false;
                    } else {
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
                        } catch (Exception e) {
                            throw new IOException(e);
                        }
                    }
                }
            }
        }

        if (isValid){
            userDao.signupUser(userName, userPhone, userPassword, userEmail, savedFilename);
        }

        session.setAttribute("form-status", errorMessages);
        resp.sendRedirect(req.getRequestURI());
    }
}