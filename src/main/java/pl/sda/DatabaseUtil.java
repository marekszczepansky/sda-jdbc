package pl.sda;

import pl.sda.dao.JdbcConnectionManager;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.stream.Collectors;

public class DatabaseUtil {

    public static void cleanUpDatabase(JdbcConnectionManager jdbcConnectionManager) throws SQLException {
        InputStream inputStream = DatabaseUtil.class.getClassLoader().getResourceAsStream("sda.sql");
        String sqlContent = new BufferedReader(new InputStreamReader(inputStream))
                .lines().collect(Collectors.joining("\n"));

        try(Connection conn = jdbcConnectionManager.getConnection()){
            conn.createStatement().executeUpdate(sqlContent);
        }
    }

}
