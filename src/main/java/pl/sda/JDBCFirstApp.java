package pl.sda;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class JDBCFirstApp {
    public static void main(String[] args) throws SQLException, IOException {

        final DbConfiguration dbConfiguration = DbConfiguration.loadConfiguration();
        final String jdbcUrl = dbConfiguration.getJdbcUrl();
        final String username = dbConfiguration.getUsername();
        final String password = dbConfiguration.getPassword();

        try (Connection conn = DriverManager.getConnection(jdbcUrl, username, password)){
            PreparedStatement ps = conn.prepareStatement("select * from car");
            ResultSet rs = ps.executeQuery();
            while (rs.next()){
                System.out.println(rs.getString("reg_number"));
            }
        }


    }

}
