package pl.sda.dao;

import pl.sda.domain.Employee;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;

/**
 * Created by pzawa on 02.02.2017.
 */
public class EmpDAOJdbcImpl extends EntityDAOImpl<Employee> implements EmpDAO {

    public EmpDAOJdbcImpl(JdbcConnectionManager jdbcConnectionManager) {
        super(jdbcConnectionManager);
    }

    private static final String QUERY_BY_ID = "select * from Emp where empno = ?";

    @Override
    public Employee findById(int id) throws Exception {
        try (Connection conn = jdbcConnectionManager.getConnection()) {
            PreparedStatement ps = conn.prepareStatement(QUERY_BY_ID);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
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
        }
        return null;
    }

    private static final String INSERT_STMT =
            "insert into Emp (empno, ename, job, manager, hiredate, salary, commision, deptno) values(?, ?, ?, ?, ?, ?, ?, ?)";

    @Override
    public void create(Employee employee) throws Exception {
        try (Connection conn = jdbcConnectionManager.getConnection()) {
            PreparedStatement ps = conn.prepareStatement(INSERT_STMT);

            ps.setInt(1, employee.getEmpno());
            ps.setString(2, employee.getEname());
            ps.setString(3, employee.getJob());
            ps.setInt(4, employee.getManager());
            ps.setDate(5, new Date(employee.getHiredate().getTime()));
            ps.setBigDecimal(6, employee.getSalary());
            ps.setBigDecimal(7, employee.getCommision());
            ps.setInt(8, employee.getDeptno());

            int numberOfAffectedRows = ps.executeUpdate();
            System.out.println("EmpDAO.create() number of affected rows: " + numberOfAffectedRows);
        }
    }

    private static final String UPDATE_STMT =
            "update emp set ename = ?, job = ?, manager = ?, hiredate = ?, salary = ?, commision = ?, deptno = ? where empno = ?";

    @Override
    public void update(Employee employee) throws Exception {
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

    private static final String DELETE_STMT = "delete from emp where empno = ?";

    @Override
    public void delete(int id) throws Exception {
        try (Connection conn = jdbcConnectionManager.getConnection()) {
            PreparedStatement ps = conn.prepareStatement(DELETE_STMT);
            ps.setInt(1, id);

            int numberOfAffectedRows = ps.executeUpdate();

            System.out.println("EmpDAO.delete() number of affected rows: " + numberOfAffectedRows);
        }
    }

    @Override
    public void create(List<Employee> employees) throws Exception {
        try (Connection conn = jdbcConnectionManager.getConnection()) {
            conn.setAutoCommit(false);
            PreparedStatement ps = conn.prepareStatement(INSERT_STMT);
            for (Employee employee : employees) {
                ps.setInt(1, employee.getEmpno());
                ps.setString(2, employee.getEname());
                ps.setString(3, employee.getJob());
                ps.setInt(4, employee.getManager());
                ps.setDate(5, new Date(employee.getHiredate().getTime()));
                ps.setBigDecimal(6, employee.getSalary());
                ps.setBigDecimal(7, employee.getCommision());
                ps.setInt(8, employee.getDeptno());
                ps.executeUpdate();
            }
            conn.commit();
        }
    }

    private static final String QUERY_SALARY_BY_DEPT = "select sum(salary) from emp where deptno = ?";

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
}
