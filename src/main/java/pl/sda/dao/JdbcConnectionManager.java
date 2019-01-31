package pl.sda.dao;

import pl.sda.DbConfiguration;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Created by pzawa on 02.02.2017.
 */
class JdbcConnectionManager {

    private static final int SCHEMA_NOT_FOUND_ERROR = 90079;
    private final DbConfiguration dbConfiguration;

    JdbcConnectionManager(DbConfiguration dbConfiguration) {
        this.dbConfiguration = dbConfiguration;
    }

    Connection getConnection() throws SQLException {
        Properties info = new Properties();
        info.setProperty("user", dbConfiguration.getUsername());
        info.setProperty("password", dbConfiguration.getPassword());
        info.setProperty("schema", dbConfiguration.getSchema());
        try {
            return DriverManager.getConnection(dbConfiguration.getJdbcUrl(), info);
        } catch (SQLException e) {
            if (e.getErrorCode() == SCHEMA_NOT_FOUND_ERROR) {
                info.remove("schema");
                return DriverManager.getConnection(dbConfiguration.getJdbcUrl(), info);
            }
            throw e;
        }
    }

}
