package pl.sda.dao;

import pl.sda.domain.Employee;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by pzawa on 02.02.2017.
 */
public interface EmpDAO extends GenericDAO<Employee> {
    BigDecimal getTotalSalaryByDept(int dept) throws Exception;

    List<Employee> findAll() throws Exception;

    List<Employee> findByJob(String name) throws Exception;
}
