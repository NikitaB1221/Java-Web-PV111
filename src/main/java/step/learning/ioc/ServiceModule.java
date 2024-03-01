package step.learning.ioc;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import step.learning.services.HashService;
import step.learning.services.Md5HashService;
import step.learning.services.db.DbService;
import step.learning.services.db.MariaDbService;
import step.learning.services.form_parse.*;
import step.learning.services.kdf.*;
import step.learning.services.rnd.*;

import java.sql.*;

public class ServiceModule extends AbstractModule implements AutoCloseable{
    @Override
    protected void configure() {
        bind(HashService.class).to(Md5HashService.class);
        bind(CodeGen.class).to(AlphaNumericCodeGen.class);
        bind(FormParseService.class).to(CommonsFormParseService.class);
        bind(KdfService.class).to(HashKdfService.class);
        bind(DbService.class).to(MariaDbService.class);
    }



    private Connection connection ;
    private  Driver mySqlDriver ;
    @Provides  // методи-провайдери: для кожної точки @Inject Connection .... буде виклик методу
    private Connection getConnection() {
        if( connection == null ) {
            try {
                mySqlDriver = new com.mysql.cj.jdbc.Driver() ;
                DriverManager.registerDriver( mySqlDriver ) ;
                String connectionString = "jdbc:mysql://localhost:3306/java_pv_111" +
                        "?useUnicode=true&characterEncoding=UTF-8" ;
                connection = DriverManager.getConnection( connectionString, "root", "" ) ;
            }
            catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        return connection ;
    }

    @Override
    public void close() throws Exception {
        if( connection != null ) {
            connection.close() ;
        }
        if( mySqlDriver != null ) {
            DriverManager.deregisterDriver( mySqlDriver ) ;
        }
    }
}
