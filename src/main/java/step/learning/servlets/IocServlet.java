package step.learning.servlets;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import step.learning.services.HashService;
import step.learning.services.rnd.CodeGen;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Singleton
public class IocServlet extends HttpServlet {

    private final HashService hashService;
    private final CodeGen codeGen;

    @Inject
    public IocServlet(HashService hashService, CodeGen codeGen) {
        this.hashService = hashService;
        this.codeGen = codeGen;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // Установка атрибутов запроса
        req.setAttribute("page-body", "ioc.jsp");
        req.setAttribute("hashService", hashService);
        req.setAttribute("codeGen", codeGen);

        req.getRequestDispatcher("WEB-INF/_layout.jsp").forward(req, resp); // Перенаправление (внутреннее)
    }
}