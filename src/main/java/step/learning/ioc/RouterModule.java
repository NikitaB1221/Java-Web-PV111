package step.learning.ioc;

import com.google.inject.servlet.ServletModule;
import step.learning.servlets.HomeServlet;
import step.learning.servlets.IocServlet;

public class RouterModule extends ServletModule {
    @Override
    protected void configureServlets() {
        serve("/").with(HomeServlet.class);
        serve("/ioc").with(IocServlet.class);
    }
}
