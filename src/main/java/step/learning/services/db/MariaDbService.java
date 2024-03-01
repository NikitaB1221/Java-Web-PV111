package step.learning.services.db;

import com.google.inject.Inject;
import com.google.inject.Provides;
import com.google.inject.Singleton;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

@Singleton
public class MariaDbService implements DbService {

    private final Logger logger;
    @Inject
    public MariaDbService(Logger logger) {
        this.logger = logger;
    }

    private Connection connection ;
    private Driver mySqlDriver ;
    @Override
    public Connection getConnection() {
        if( connection == null ) {
            try {
                mySqlDriver = new com.mysql.cj.jdbc.Driver() ;
                DriverManager.registerDriver( mySqlDriver ) ;
                String connectionString = "jdbc:mysql://localhost:3306/java_111" +
                        "?useUnicode=true&characterEncoding=UTF-8" ;
                connection = DriverManager.getConnection( connectionString, "root", "" ) ;
            }
            catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        return connection ;
    }

    public void closeConnection() {
        try {
            if (connection != null) {
                connection.close();
            }
            if (mySqlDriver != null) {
                DriverManager.deregisterDriver(mySqlDriver);
            }
        }
        catch (SQLException ex) {
            logger.log(Level.WARNING, ex.getMessage());
        }
    }
}
