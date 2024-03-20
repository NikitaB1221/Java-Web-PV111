package step.learning.filters;

import com.google.inject.Singleton;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.logging.Logger;

@Singleton
public class EncodingFilter implements Filter {

    private FilterConfig filterConfig;
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        this.filterConfig = filterConfig;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        String path = httpRequest.getRequestURI().substring(httpRequest.getContextPath().length());

        if (path.startsWith("/css/") || path.startsWith("/js/") || path.startsWith("/upload/") || path.startsWith("/img/")) {
            // Пропустить фильтрацию для этих путей
            chain.doFilter(request, response);
        } else {
            httpRequest.setCharacterEncoding("UTF-8");
            httpResponse.setCharacterEncoding("UTF-8");
            chain.doFilter(httpRequest, httpResponse);
        }
    }

    @Override
    public void destroy() {
        this.filterConfig = null;
    }
}
