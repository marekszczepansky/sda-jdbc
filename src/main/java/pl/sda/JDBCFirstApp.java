package pl.sda;

import pl.sda.dao.DeptDAOJdbcImpl;
import pl.sda.dao.EmpDAOJdbcImpl;
import pl.sda.dao.JdbcConnectionManager;

import java.io.IOException;
import java.sql.SQLException;

public class JDBCFirstApp {
    public static void main(String[] args) throws SQLException, IOException {
        JdbcConnectionManager jdbcConnectionManager = new JdbcConnectionManager(DbConfiguration.loadConfiguration());
        DatabaseUtil.cleanUpDatabase(jdbcConnectionManager);

        EmpDAOJdbcImpl empDAO = new EmpDAOJdbcImpl(jdbcConnectionManager);
        DeptDAOJdbcImpl deptDAO = new DeptDAOJdbcImpl(jdbcConnectionManager);




    }


}
