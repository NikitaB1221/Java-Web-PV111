package step.learning.filters;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import step.learning.entity.User;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

@Singleton
public class AuthFilter implements Filter {
    private FilterConfig filterConfig;
    private final Logger logger ;

    @Inject
    public AuthFilter(Logger logger) {
        this.logger = logger;
    }


    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        this.filterConfig = filterConfig;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        // прямий хід: запит -> view (jsp)
        HttpServletRequest req = (HttpServletRequest) request;
        HttpSession session = req.getSession();
        User user = (User) session.getAttribute("auth-user");
        if(req.getParameterMap().containsKey("logout")) {
            session.removeAttribute("auth-user");
            ((HttpServletResponse)response).sendRedirect(((HttpServletRequest) request).getRequestURI());
            return;
        }
        if( user != null ) {
            req.setAttribute("auth-user", user);
            logger.log(Level.INFO, "AuthFilter: user authorized");
        }
        else {
            logger.log(Level.INFO, "AuthFilter: user unauthorized");
        }
        chain.doFilter(request, response);  // передача роботу наступному фільтру/сервлету
        // зворотній хід: view (jsp) -> response
    }

    @Override
    public void destroy() {
        this.filterConfig = null;
    }
}
/*
Сервлетні фільтри - реалізація концепції MiddleWare
Реєструються фільтри аналогічно сервлетам
або) у web.xml
або) анотацією WebFilter("шлях")
або) у налаштуваннях IoC (RouterModule) - не забути @Singleton
--------------------
Задача - перенести управління підключенням БД до фільтрів (з IoC)
- Створюємо сервіс DbService, переносимо роботу з підключенням до нього
- Створюємо статичну сторінку (static.jsp), яка буде включатись при проблемах з БД
- Створюємо фільтр, який тестує підключення до БД і, у разі збою, переводить
   на статичну сторінку
 */