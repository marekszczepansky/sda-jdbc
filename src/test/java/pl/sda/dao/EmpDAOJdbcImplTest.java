package pl.sda.dao;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import pl.sda.DbConfiguration;
import pl.sda.domain.Employee;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by pzawa on 02.02.2017.
 */
public class EmpDAOJdbcImplTest {
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    private EmpDAO empDAO;

    @Before
    public void init() throws IOException, ClassNotFoundException, SQLException {
        JdbcConnectionManager jdbcConnectionManager = new JdbcConnectionManager(DbConfiguration.loadConfiguration());
        empDAO =  new EmpDAOJdbcImpl(jdbcConnectionManager);
        TestUtil.cleanUpDatabase(jdbcConnectionManager);
    }

    @Test
    public void findById() throws Exception {
        Employee employee = empDAO.findById(7369);

        assertNotNull(employee);
        assertEquals(20, employee.getDeptno());
        assertEquals("SMITH", employee.getEname());
        assertEquals("CLERK", employee.getJob());
        assertEquals(sdf.parse("1993-06-13"), employee.getHiredate());
        assertEquals(BigDecimal.valueOf(800), employee.getSalary());
        assertTrue(BigDecimal.valueOf(800.00).compareTo(employee.getSalary())==0);
        assertTrue(BigDecimal.valueOf(0.00).compareTo(employee.getCommision())==0);

    }

    @Test
    public void create() throws Exception {
        Employee newEmployee = new Employee(9000, "JKOWALSKI", "Manager", 7839, sdf.parse("2017-01-01"), BigDecimal.valueOf(10000), BigDecimal.valueOf(10.0), 20);

        empDAO.create(newEmployee);

        Employee employeeFromDB = empDAO.findById(9000);

        assertNotNull(employeeFromDB);
        assertEquals(employeeFromDB.getEmpno(), newEmployee.getDeptno());
        assertEquals(employeeFromDB.getEname(), newEmployee.getEname());
        assertEquals(employeeFromDB.getJob(), newEmployee.getJob());
        assertEquals(employeeFromDB.getHiredate(), newEmployee.getHiredate());
        assertEquals(employeeFromDB.getSalary(), newEmployee.getSalary());
        assertEquals(employeeFromDB.getCommision(), newEmployee.getCommision());
        assertEquals(employeeFromDB.getDeptno(), newEmployee.getDeptno());
    }

    @Test
    public void update() throws Exception {
        Employee employee = empDAO.findById(7369);
        assertNotNull(employee);

        employee.setJob("SUPERCLERK");
        empDAO.update(employee);
        employee = empDAO.findById(7369);

        assertNotNull(employee);
        assertEquals(7369, employee.getDeptno());
        assertEquals("SMITH", employee.getEname());
        assertEquals("SUPERCLERK", employee.getJob());
        assertEquals(sdf.parse("1993-06-13"), employee.getHiredate());
        assertEquals(BigDecimal.valueOf(800), employee.getSalary());
        assertEquals(BigDecimal.valueOf(0.0), employee.getCommision());
        assertEquals(20, employee.getDeptno());

    }

    @Test
    public void delete() throws Exception {
        Employee employee = empDAO.findById(7369);
        assertNotNull(employee);

        empDAO.delete(7369);

        employee = empDAO.findById(7369);
        assertNull(employee);
    }

    @Test
    public void createMultipleEmployeesAllOk() throws Exception {
        Employee newEmployee1 = new Employee(9000, "JKOWALSKI", "Manager", 7839, sdf.parse("2017-01-01"), BigDecimal.valueOf(10000), BigDecimal.valueOf(10.0), 20);
        Employee newEmployee2 = new Employee(9001, "JNOWAK", "Manager", 7839, sdf.parse("2017-01-01"), BigDecimal.valueOf(10001), BigDecimal.valueOf(9.0), 30);

        List<Employee> employeeList = new ArrayList<>();
        employeeList.add(newEmployee1);
        employeeList.add(newEmployee2);

        empDAO.create(employeeList);

        Employee employeeFromDB1 = empDAO.findById(9000);
        Employee employeeFromDB2 = empDAO.findById(9001);

        assertNotNull(employeeFromDB1);
        assertEquals(employeeFromDB1.getEmpno(), newEmployee1.getDeptno());
        assertEquals(employeeFromDB1.getEname(), newEmployee1.getEname());
        assertEquals(employeeFromDB1.getJob(), newEmployee1.getJob());
        assertEquals(employeeFromDB1.getHiredate(), newEmployee1.getHiredate());
        assertEquals(employeeFromDB1.getSalary(), newEmployee1.getSalary());
        assertEquals(employeeFromDB1.getCommision(), newEmployee1.getCommision());
        assertEquals(employeeFromDB1.getDeptno(), newEmployee1.getDeptno());

        assertNotNull(employeeFromDB2);
        assertEquals(employeeFromDB2.getEmpno(), newEmployee2.getDeptno());
        assertEquals(employeeFromDB2.getEname(), newEmployee2.getEname());
        assertEquals(employeeFromDB2.getJob(), newEmployee2.getJob());
        assertEquals(employeeFromDB2.getHiredate(), newEmployee2.getHiredate());
        assertEquals(employeeFromDB2.getSalary(), newEmployee2.getSalary());
        assertEquals(employeeFromDB2.getCommision(), newEmployee2.getCommision());
        assertEquals(employeeFromDB2.getDeptno(), newEmployee2.getDeptno());
    }

    @Test(expected=SQLException.class)
    public void createMultipleEmployeesSecondRowFail() throws Exception {
        Employee newEmployee1 = new Employee(9000, "JKOWALSKI", "Manager", 7839, sdf.parse("2017-01-01"), BigDecimal.valueOf(10000), BigDecimal.valueOf(10.0), 20);
        Employee newEmployee2 = new Employee(9000, "JNOWAK", "Manager", 7839, sdf.parse("2017-01-01"), BigDecimal.valueOf(10001), BigDecimal.valueOf(9.0), 30);

        List<Employee> employeeList = new ArrayList<>();
        employeeList.add(newEmployee1);
        employeeList.add(newEmployee2);

        try {
            empDAO.create(employeeList);
        }catch(Exception ex){
            Employee employeeFromDB = empDAO.findById(9000);
            assertNull(employeeFromDB);
            throw ex;
        }
    }

    @Test
    public void getTotalSalaryByDept() throws Exception {
        BigDecimal salaryFor10Dept = empDAO.getTotalSalaryByDept(10);

        assertEquals(8750, salaryFor10Dept);
    }

}
