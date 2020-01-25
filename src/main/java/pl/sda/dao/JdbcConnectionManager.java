package pl.sda.dao;

import pl.sda.DbConfiguration;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Created by pzawa on 02.02.2017.
 */
public class JdbcConnectionManager {

    private final DbConfiguration dbConfiguration;

    public JdbcConnectionManager(DbConfiguration dbConfiguration) {
        this.dbConfiguration = dbConfiguration;
    }

    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(dbConfiguration.getJdbcUrl(), dbConfiguration.getUsername(), dbConfiguration.getPassword());
    }

}
