package step.learning.servlets;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import step.learning.dal.NewsDao;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Singleton
public class NewsServlet  extends HttpServlet {
    private final NewsDao newsDao;
    @Inject
    public NewsServlet(NewsDao newsDao) {
        this.newsDao = newsDao;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
       req.setAttribute("create-status",newsDao.installTable()
               ? "Success"
               : "Error");
        req.setAttribute("page-body", "news.jsp");
        req.getRequestDispatcher("WEB-INF/_layout.jsp")  // перенаправлення (внутрішнє)
                .forward(req, resp);
    }
}
