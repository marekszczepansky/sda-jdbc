package pl.sda.domain;

import java.util.Objects;

/**
 * Created by pzawa on 02.02.2017.
 */
public class Department {
    private int deptno;
    private String dname;
    private String location;


    public Department() {
    }

    public Department(int deptno, String dname, String location) {
        this.deptno = deptno;
        this.dname = dname;
        this.location = location;
    }

    public int getDeptno() {
        return deptno;
    }

    public void setDeptno(int deptno) {
        this.deptno = deptno;
    }

    public String getDname() {
        return dname;
    }

    public void setDname(String dname) {
        this.dname = dname;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Department that = (Department) o;
        return deptno == that.deptno;
    }

    @Override
    public int hashCode() {
        return Objects.hash(deptno);
    }

    @Override
    public String toString() {
        return "Department{" +
                "deptno=" + deptno +
                ", dname='" + dname + '\'' +
                ", location='" + location + '\'' +
                '}';
    }
}
