package step.learning.ioc;

import com.google.inject.AbstractModule;
import step.learning.services.HashService;
import step.learning.services.Md5HashService;
import step.learning.services.form_parse.CommonsFormParseService;
import step.learning.services.form_parse.FormParseService;
import step.learning.services.rnd.AlphaNumericCodeGen;
import step.learning.services.rnd.CodeGen;

public class ServiceModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(HashService.class).to(Md5HashService.class);
        bind(CodeGen.class).to(AlphaNumericCodeGen.class);
        bind(FormParseService.class).to(CommonsFormParseService.class);
    }
}
