package pl.sda.dao;

import pl.sda.domain.Employee;

import java.math.BigDecimal;
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
    public Employee findById(int id) throws Exception {
        return null;
    }

    @Override
    public void create(Employee employee) throws Exception {

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
