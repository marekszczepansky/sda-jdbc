package pl.sda.dao;

import org.junit.Before;
import org.junit.Test;
import pl.sda.DatabaseUtil;
import pl.sda.DbConfiguration;
import pl.sda.domain.Employee;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * Created by pzawa on 02.02.2017.
 */
public class EmpDAOJdbcImplTest {
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    private EmpDAO empDAO;

    @Before
    public void init() throws IOException, SQLException {
        JdbcConnectionManager jdbcConnectionManager = new JdbcConnectionManager(DbConfiguration.loadConfiguration());
        empDAO =  new EmpDAOJdbcImpl(jdbcConnectionManager);
        DatabaseUtil.cleanUpDatabase(jdbcConnectionManager);
    }

    @Test
    public void findById() throws Exception {
        Employee employee = empDAO.findById(7369);

        assertNotNull(employee);
        assertEquals(20, employee.getDeptno());
        assertEquals("SMITH", employee.getEname());
        assertEquals("CLERK", employee.getJob());
        assertEquals(sdf.parse("1993-06-13"), employee.getHiredate());
        assertTrue(BigDecimal.valueOf(800.00).compareTo(employee.getSalary())==0);
        assertTrue(BigDecimal.valueOf(0.00).compareTo(employee.getCommision())==0);

    }

    @Test
    public void create() throws Exception {
        Employee newEmployee = new Employee(9000, "JKOWALSKI", "Manager", 7839, sdf.parse("2017-01-01"), BigDecimal.valueOf(10000), BigDecimal.valueOf(10.0), 20);

        empDAO.create(newEmployee);

        Employee employeeFromDB = empDAO.findById(9000);

        assertNotNull(employeeFromDB);
        assertEquals(employeeFromDB.getEmpno(), newEmployee.getEmpno());
        assertEquals(employeeFromDB.getEname(), newEmployee.getEname());
        assertEquals(employeeFromDB.getJob(), newEmployee.getJob());
        assertEquals(employeeFromDB.getHiredate(), newEmployee.getHiredate());
        assertTrue(employeeFromDB.getSalary().compareTo(newEmployee.getSalary()) ==0);
        assertTrue(employeeFromDB.getCommision().compareTo(newEmployee.getCommision()) ==0);
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
        assertEquals(20, employee.getDeptno());
        assertEquals("SMITH", employee.getEname());
        assertEquals("SUPERCLERK", employee.getJob());
        assertEquals(sdf.parse("1993-06-13"), employee.getHiredate());
        assertTrue(BigDecimal.valueOf(800).compareTo(employee.getSalary()) == 0);
        assertTrue(BigDecimal.valueOf(800.00).compareTo(employee.getSalary()) == 0);

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
        assertEquals(employeeFromDB1.getEmpno(), newEmployee1.getEmpno());
        assertEquals(employeeFromDB1.getEname(), newEmployee1.getEname());
        assertEquals(employeeFromDB1.getJob(), newEmployee1.getJob());
        assertEquals(employeeFromDB1.getHiredate(), newEmployee1.getHiredate());
        assertTrue(employeeFromDB1.getSalary().compareTo(newEmployee1.getSalary())==0);
        assertTrue(employeeFromDB1.getCommision().compareTo(newEmployee1.getCommision())==0);

        assertNotNull(employeeFromDB2);
        assertEquals(employeeFromDB2.getEmpno(), newEmployee2.getEmpno());
        assertEquals(employeeFromDB2.getEname(), newEmployee2.getEname());
        assertEquals(employeeFromDB2.getJob(), newEmployee2.getJob());
        assertEquals(employeeFromDB2.getHiredate(), newEmployee2.getHiredate());
        assertTrue(employeeFromDB2.getSalary().compareTo(newEmployee2.getSalary())==0);
        assertTrue(employeeFromDB2.getCommision().compareTo(newEmployee2.getCommision())==0);
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

        assertTrue(new BigDecimal("8750").compareTo(salaryFor10Dept) == 0);
    }

    @Test
    public void findAll() throws Exception {
        List<Employee> employeeList = empDAO.findAll();
        assertEquals(14, employeeList.size());

        Employee employee1 = employeeList.get(0);
        assertEquals(20, employee1.getDeptno());
        assertEquals("SMITH", employee1.getEname());
        assertEquals("CLERK", employee1.getJob());

        Employee employee2 = employeeList.get(13);
        assertEquals(30, employee2.getDeptno());
        assertEquals("MARTIN", employee2.getEname());
        assertEquals("SALESMAN", employee2.getJob());
    }

    @Test
    public void findByJob() throws Exception {
        final String jobName = "SALESMAN";
        List<Employee> byJob = empDAO.findByJob(jobName);
        assertEquals(4, byJob.size());

        Employee employee1 = byJob.get(0);
        assertEquals(30, employee1.getDeptno());
        assertEquals("ALLEN", employee1.getEname());
        assertEquals(jobName, employee1.getJob());

        Employee employee2 = byJob.get(3);
        assertEquals(30, employee2.getDeptno());
        assertEquals("MARTIN", employee2.getEname());
        assertEquals(jobName, employee2.getJob());

        for (Employee employee : byJob) {
            assertEquals(jobName, employee.getJob());
        }
    }

}
