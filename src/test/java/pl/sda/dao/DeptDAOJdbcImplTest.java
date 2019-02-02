package pl.sda.dao;

import org.junit.Before;
import org.junit.Test;
import pl.sda.DbConfiguration;
import pl.sda.domain.Department;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.Assert.*;

/**
 * Created by pzawa on 02.02.2017.
 */
public class DeptDAOJdbcImplTest {

    private DeptDAO deptDAO;

    @Before
    public void init() throws IOException, ClassNotFoundException, SQLException {
        JdbcConnectionManager jdbcConnectionManager = new JdbcConnectionManager(DbConfiguration.loadConfiguration());
        deptDAO = new DeptDAOJdbcImpl(jdbcConnectionManager);
        TestUtil.cleanUpDatabase(jdbcConnectionManager);
    }

    @Test
    public void findById() throws Exception {
        Department department = deptDAO.findById(10);
        assertNotNull(department);
        assertEquals(10, department.getDeptno());
        assertEquals("Accounting", department.getDname());
        assertEquals("New York", department.getLocation());
    }

    @Test
    public void create() throws Exception {
        Department department = new Department(99, "HR", "Las Vegas");
        deptDAO.create(department);

        Department departmentFromDb = deptDAO.findById(99);

        assertNotNull(departmentFromDb);
        assertEquals(department.getDeptno(), departmentFromDb.getDeptno());
        assertEquals(department.getDname(), departmentFromDb.getDname());
        assertEquals(department.getLocation(), departmentFromDb.getLocation());

    }

    @Test
    public void update() throws Exception {
        Department department = deptDAO.findById(10);
        assertNotNull(department);

        department.setDname("NEW_DEPT");
        deptDAO.update(department);

        department = deptDAO.findById(10);

        assertNotNull(department);
        assertEquals(10, department.getDeptno());
        assertEquals("NEW_DEPT", department.getDname());
        assertEquals("New York", department.getLocation());
    }

    @Test
    public void delete() throws Exception {
        Department department = deptDAO.findById(40);
        assertNotNull(department);

        deptDAO.delete(40);

        department = deptDAO.findById(40);
        assertNull(department);
    }

    @Test
    public void createMultipleDepartmentsAllOk() {
        fail("not implemented");
    }

    @Test
    public void createMultipleDepartmentsSecondFail() {
        fail("not implemented");
    }
}