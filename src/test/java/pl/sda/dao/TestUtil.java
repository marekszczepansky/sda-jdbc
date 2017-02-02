package pl.sda.dao;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.stream.Collectors;

/**
 * Created by pzawa on 02.02.2017.
 */
public class TestUtil {

    public static void cleanUpDatabase(JdbcConnectionManager jdbcConnectionManager) throws IOException, SQLException {
        InputStream inputStream = TestUtil.class.getClassLoader().getResourceAsStream("sda.sql");
        String sqlContent = new BufferedReader(new InputStreamReader(inputStream))
                .lines().collect(Collectors.joining("\n"));

        try(Connection conn = jdbcConnectionManager.getConnection()){
            conn.createStatement().executeUpdate(sqlContent);
        }
    }

}
