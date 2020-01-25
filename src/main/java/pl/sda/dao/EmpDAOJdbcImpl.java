package pl.sda.dao;

import pl.sda.domain.Employee;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by pzawa on 02.02.2017.
 */
public class EmpDAOJdbcImpl extends EntityDAOImpl<Employee> implements EmpDAO {

    public static final String FIND_BY_JOB = "select * from emp where job = ?";
    public static final String FIND_ALL = "select * from emp";

    private static final String QUERY_BY_ID = "select * from Emp where empno = ?";
    private static final String INSERT_STMT = "insert into Emp (empno, ename, job, manager, hiredate, salary, commision, deptno) values(?, ?, ?, ?, ?, ?, ?, ?)";
    private static final String UPDATE_STMT = "update emp set ename = ?, job = ?, manager = ?, hiredate = ?, salary = ?, commision = ?, deptno = ? where empno = ?";
    private static final String DELETE_STMT = "delete from emp where empno = ?";
    private static final String QUERY_SALARY_BY_DEPT = "select sum(salary) from emp where deptno = ?";

    public EmpDAOJdbcImpl(JdbcConnectionManager jdbcConnectionManager) {
        super(jdbcConnectionManager);
    }

    @Override
    public Employee findById(int id) throws SQLException {
        try (Connection conn = jdbcConnectionManager.getConnection()) {
            PreparedStatement ps = conn.prepareStatement(QUERY_BY_ID);
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
            PreparedStatement ps = conn.prepareStatement(INSERT_STMT);

            setCreateStatement(employee, ps);

            int numberOfAffectedRows = ps.executeUpdate();

            System.out.println("EmpDAO.create() number of affected rows: " + numberOfAffectedRows);
        }

    }

    private void setCreateStatement(Employee employee, PreparedStatement ps) throws SQLException {
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
    public void update(Employee employee) throws SQLException {
        try (Connection conn = jdbcConnectionManager.getConnection()) {
            PreparedStatement ps = conn.prepareStatement(UPDATE_STMT);

            ps.setString(1, employee.getEname());
            ps.setString(2, employee.getJob());
            ps.setInt(3, employee.getManager());
            ps.setDate(4, new Date(employee.getHiredate().getTime()));
            ps.setBigDecimal(5, employee.getSalary());
            ps.setBigDecimal(6, employee.getCommision());
            ps.setInt(7, employee.getDeptno());

            ps.setInt(8, employee.getEmpno());

            int numberOfAffectedRows = ps.executeUpdate();

            System.out.println("EmpDAO.update() number of affected rows: " + numberOfAffectedRows);
        }
    }

    @Override
    public void delete(int id) throws SQLException {
        try (Connection conn = jdbcConnectionManager.getConnection()) {
            PreparedStatement ps = conn.prepareStatement(DELETE_STMT);
            ps.setInt(1, id);

            int numberOfAffectedRows = ps.executeUpdate();

            System.out.println("EmpDAO.delete() number of affected rows: " + numberOfAffectedRows);
        }
    }

    @Override
    public void create(List<Employee> employees) throws SQLException {
        try (Connection conn = jdbcConnectionManager.getConnection()) {
            conn.setAutoCommit(false);
            PreparedStatement ps = conn.prepareStatement(INSERT_STMT);
            for (Employee employee : employees) {
                setCreateStatement(employee, ps);
                ps.executeUpdate();
            }
            conn.commit();
        }
    }

    @Override
    public BigDecimal getTotalSalaryByDept(int dept) throws Exception {
        try (Connection conn = jdbcConnectionManager.getConnection()) {
            PreparedStatement ps = conn.prepareStatement(QUERY_SALARY_BY_DEPT);
            ps.setInt(1, dept);

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getBigDecimal(1);
            }
        }
        return null;
    }

    @Override
    public List<Employee> findAll() throws Exception {
        try (Connection conn = jdbcConnectionManager.getConnection()) {
            Statement statement = conn.createStatement();
            ResultSet resultSet = statement.executeQuery(FIND_ALL);
            List<Employee> result = new ArrayList<>();
            while(resultSet.next()) {
                result.add(mapFromResultSet(resultSet));
            }
            return result;
        }
    }

    @Override
    public List<Employee> findByJob(String name) throws Exception {
        try (Connection conn = jdbcConnectionManager.getConnection()) {
            PreparedStatement preparedStatement = conn.prepareStatement(FIND_BY_JOB);
            preparedStatement.setString(1, name);
            ResultSet resultSet = preparedStatement.executeQuery();
            List<Employee> result = new ArrayList<>();
            while(resultSet.next()) {
                result.add(mapFromResultSet(resultSet));
            }
            return result;
        }
    }
}
