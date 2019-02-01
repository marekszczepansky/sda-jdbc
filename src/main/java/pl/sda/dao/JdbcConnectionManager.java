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

    private final DbConfiguration dbConfiguration;

    JdbcConnectionManager(DbConfiguration dbConfiguration) {
        this.dbConfiguration = dbConfiguration;
    }

    Connection getConnection() throws SQLException {
        return DriverManager.getConnection(dbConfiguration.getJdbcUrl(), dbConfiguration.getUsername(), dbConfiguration.getPassword());
    }

}
