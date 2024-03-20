package step.learning.ioc;

import com.google.inject.servlet.ServletModule;
import step.learning.filters.AuthFilter;
import step.learning.filters.DbFilter;
import step.learning.filters.EncodingFilter;
import step.learning.servlets.*;

public class RouterModule extends ServletModule {
    @Override
    protected void configureServlets() {
        filterRegex("^/(?!css/.+|js/.+|img/.+|upload/.+|resources/.+).*$").through(EncodingFilter.class);
        filterRegex("^/(?!css/.+|js/.+|img/.+|resources/.+).*$").through(DbFilter.class);
        filter("/*").through(AuthFilter.class);

        serve("/").with(HomeServlet.class);
        serve("/ioc").with(IocServlet.class);
        serve("/signup").with(SignUpServlet.class);
        serve("/auth").with(AuthServlet.class);
        serve("/privacy").with(PrivacyServlet.class);
//        serve("/news").with(NewsServlet.class);
        serve("/news/*").with(NewsServlet.class);
        serve("/profile/*").with(ProfileServlet.class);
        serve("/comment").with(CommentServlet.class);
    }
}
