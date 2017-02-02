package pl.sda;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

/**
 * Created by pzawa on 02.02.2017.
 */
public class DbConfiguration {
    private final String jdbcUrl;
    private final String username;
    private final String password;
    private final String schema;

    private DbConfiguration(String jdbcUrl, String username, String password, String schema) {
        this.jdbcUrl = jdbcUrl;
        this.username = username;
        this.password = password;
        this.schema = schema;
    }

    public String getJdbcUrl() {
        return jdbcUrl;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getSchema() {
        return schema;
    }

    @Override
    public String toString() {
        return "DbConfiguration{" +
                "jdbcUrl='" + jdbcUrl + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", schema='" + schema + '\'' +
                '}';
    }

    public static DbConfiguration loadConfiguration() throws IOException {
        Properties props = new Properties();
        props.load(DbConfiguration.class.getClassLoader()
                .getResourceAsStream("dbconfig.properties"));
        DbConfiguration dbConfiguration = new DbConfiguration(props.getProperty("jdbcurl"), props.getProperty("username"), props.getProperty("password"), props.getProperty("schema"));
        System.out.println("Configuration created with the following values: " + dbConfiguration.toString());

        return dbConfiguration;
    }
}
