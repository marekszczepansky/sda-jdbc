package pl.sda.dao;

import pl.sda.domain.Employee;

import java.math.BigDecimal;
import java.util.List;

public interface EmpDAO extends EntityDAO<Employee> {

    BigDecimal getTotalSalaryByDept(int dept) throws Exception;

    List<Employee> findAll() throws Exception;

    List<Employee> findByJob(String name) throws Exception;
}
