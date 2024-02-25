package step.learning.ioc;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.servlet.GuiceServletContextListener;

/**
 * Listener(subscriber, handler) of the project context creation event
 * launch. To subscribe we go next to web.xml
 */
public class IosContextListener extends GuiceServletContextListener {

    @Override
    protected Injector getInjector() {
        return Guice.createInjector(
                new ServiceModule(),
                new RouterModule(),
                new LoggerModule()
        );
    }
}
