package pl.sda.dao;

import pl.sda.domain.Employee;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by pzawa on 02.02.2017.
 */
public class EmpDAOJdbcImpl implements EmpDAO {

    private final JdbcConnectionManager jdbcConnectionManager;

    public EmpDAOJdbcImpl(JdbcConnectionManager jdbcConnectionManager){
        this.jdbcConnectionManager = jdbcConnectionManager;
    }
    @Override
    public Employee findById(int id) throws SQLException {
        try (Connection conn = jdbcConnectionManager.getConnection()) {
            PreparedStatement ps = conn.prepareStatement("select * from Emp where empno = ?");
            ps.setInt(1, id);

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return mapFromResultSet(rs);
            }

        }
        return null;
    }

    private Employee mapFromResultSet(ResultSet rs) throws SQLException {
        return new Employee(
                rs.getInt("empno"),
                rs.getString("ename"),
                rs.getString("job"),
                rs.getInt("manager"),
                rs.getDate("hiredate"),
                rs.getBigDecimal("salary"),
                rs.getBigDecimal("commision"),
                rs.getInt("deptno")
        );
    }

    @Override
    public void create(Employee employee) throws SQLException {
        try (Connection conn = jdbcConnectionManager.getConnection()) {
            PreparedStatement ps = conn.prepareStatement(
                    "insert into Emp (empno, ename, job, manager, hiredate, salary, commision, deptno) values(?, ?, ?, ?, ?, ?, ?, ?)");

            prepareStatement(employee, ps);

            int numberOfAffectedRows = ps.executeUpdate();
            System.out.println("EmpDAO.create() number of affected rows: " + numberOfAffectedRows);
        }
    }

    private void prepareStatement(Employee employee, PreparedStatement ps) throws SQLException {
        ps.setInt(1, employee.getEmpno());
        ps.setString(2, employee.getEname());
        ps.setString(3, employee.getJob());
        ps.setInt(4, employee.getManager());
        ps.setDate(5, new Date(employee.getHiredate().getTime()));
        ps.setBigDecimal(6, employee.getSalary());
        ps.setBigDecimal(7, employee.getCommision());
        ps.setInt(8, employee.getDeptno());
    }

    @Override
    public void update(Employee employee) throws Exception {

    }

    @Override
    public void delete(int id) throws Exception {

    }

    @Override
    public void create(List<Employee> employees) throws Exception {
    }

    @Override
    public BigDecimal getTotalSalaryByDept(int dept) throws Exception {
        return null;
    }
}
