package pl.sda.dao;

import pl.sda.domain.Department;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by pzawa on 02.02.2017.
 */
public class DeptDAOJdbcImpl implements GenericDAO<Department>{
    private static String QUERY_BY_ID  = "SELECT deptno, dname, location FROM Dept WHERE deptno = ?";
    private static String INSERT_STMT = "INSERT INTO Dept(deptno, dname, location) VALUES(?,?,?)";
    private static String UPDATE_STMT= "UPDATE Dept set dname = ?, location = ?  WHERE deptno = ?";
    private static String DELETE_STMT= "DELETE FROM Dept WHERE deptno = ?";

    private final JdbcConnectionManager jdbcConnectionManager;

    public DeptDAOJdbcImpl(JdbcConnectionManager jdbcConnectionManager) {
        this.jdbcConnectionManager = jdbcConnectionManager;
    }

    @Override
    public Department findById(int id) throws SQLException {
        try(Connection conn = jdbcConnectionManager.getConnection()) {
            PreparedStatement ps = conn.prepareStatement(QUERY_BY_ID);
            ps.setInt(1, id);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Department department = mapFromResultSet(rs);
                return department;
            }

        }
        return null;
    }

    private Department mapFromResultSet(ResultSet rs) throws SQLException {
        int deptno = rs.getInt("deptno");
        String dname = rs.getString("dname");
        String location = rs.getString("location");

        return new Department(deptno, dname, location);

    }

    @Override
    public void create(Department department) throws SQLException {
        try (Connection conn = jdbcConnectionManager.getConnection()){
            PreparedStatement ps = conn.prepareStatement(INSERT_STMT);
            ps.setInt(1, department.getDeptno());
            ps.setString(2, department.getDname());
            ps.setString(3, department.getLocation());

            int numberOfAffectedRows = ps.executeUpdate();

            System.out.println("DeptDAO.create() number of affected rows: " + numberOfAffectedRows);
        }
    }

    @Override
    public void update(Department department) throws SQLException {
        try(Connection conn = jdbcConnectionManager.getConnection()) {
            PreparedStatement ps = conn.prepareStatement(UPDATE_STMT);
            ps.setString(1, department.getDname());
            ps.setString(2, department.getLocation());
            ps.setInt(3, department.getDeptno());

            int numberOfAffectedRows = ps.executeUpdate();

            System.out.println("DeptDAO.update() number of affected rows: " + numberOfAffectedRows);
        }
    }

    @Override
    public void delete(int id) throws SQLException {
        try(Connection conn = jdbcConnectionManager.getConnection()) {
            PreparedStatement ps = conn.prepareStatement(DELETE_STMT);
            ps.setInt(1, id);

            int numberOfAffectedRows = ps.executeUpdate();

            System.out.println("DeptDAO.delete() number of affected rows: " + numberOfAffectedRows);
        }
    }

    @Override
    public void create(List<Department> employees) throws Exception {

    }
}
