package pl.sda.dao;

import pl.sda.domain.Department;

import java.util.List;

public interface DeptDAO extends EntityDAO<Department> {

    public void create(List<Department> employees) throws Exception;
}
