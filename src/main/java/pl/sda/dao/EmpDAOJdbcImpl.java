package pl.sda.dao;

import pl.sda.domain.Department;
import pl.sda.domain.Employee;

import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLType;
import java.util.Arrays;
import java.util.List;
import java.util.stream.StreamSupport;

/**
 * Created by pzawa on 02.02.2017.
 */
public class EmpDAOJdbcImpl implements EmpDAO {
	
	private static String QUERY_BY_ID  = "SELECT empno, ename, job, manager, hiredate, salary, commision, deptno FROM Emp WHERE empno = ?";
    private static String INSERT_STMT = "INSERT INTO Emp(empno, ename, job, manager, hiredate, salary, commision, deptno) VALUES(?,?,?,?,?,?,?,?)";
    private static String UPDATE_STMT= "UPDATE Emp set ename = ?, job = ?, manager = ?, hiredate = ?, salary = ?, commision = ?, deptno = ?  WHERE empno = ?";
    private static String DELETE_STMT= "DELETE FROM Emp WHERE empno = ?";
    private static String QUERY_TOTAL_SALARY_BY_DEPT = "{ ? = call sda.calculate_salary_by_dept(?)}";

    private final JdbcConnectionManager jdbcConnectionManager;

    public EmpDAOJdbcImpl(JdbcConnectionManager jdbcConnectionManager){
        this.jdbcConnectionManager = jdbcConnectionManager;
    }

    @Override
    public Employee findById(int id) throws Exception {
        //Nawiązujemy połaczenie z bazą danych. Metoda jdbcConnectionManager.getConnection() zwraca nowe połączenie z bazą
        //Połaczenie znajduje się w bloku try with resources który zapewnia zamknięcie połączenia po zakończeniu bloku
        //try(Pobranie zasobu, np polaczenia){
        //   jakies operacje,
        //}
        try(Connection conn = jdbcConnectionManager.getConnection()) {
            //Stworzenie obiektu ktory reprezentuje polecenie sql w bazie danych. Do obiektu przekazujemy tresc zapytania, czyli np.
            //"SELECT empno, ename, job, manager, hiredate, salary, commision, deptno FROM Emp WHERE empno = ?"
            //Zapytanie pobiera z bazy danych dane pracoownika ktorego identyfikator (empno) jest rowny zadanemu parametrowi
            //Pobierane sa nastepujace dane pracownika: empno, ename, job, manager, hiredate, salary, commision, deptno
            PreparedStatement ps = conn.prepareStatement(QUERY_BY_ID);
            //Ustawiamy wartosc podstawiona pod pierwszy znak zapytania
            //Poniewaz id jest typu int  uzywamy ps.setInt
            ps.setInt(1, id);

            //Wykonujemy zapytanie (utworzone wczesniej)
            ResultSet rs = ps.executeQuery();
            //Przechodzimy od pierwszego do ostatniego wyniku zapytania  rs.next() pobiera kolejny wiersz. Gdy
            //rs.next() zwroci false konczy sie petla while(){] i wychodzimy z funkcji
            while (rs.next()) {
                //Wywolujemy funkcje prywatna ktora skonwertuje wiersz ResultSet do obiektu Employee
                //Kod funkcji ponizej
                Employee emp = mapFromResultSet(rs);
                return emp;
            }

        }
        return null;
    }
    
    private Employee mapFromResultSet(ResultSet rs) throws SQLException {
        //Pobieramy kazda kolumne z wiersza z bazy danych. Musimy pamietac ze tekst w rs.getInt("empno") okresla nazwe kolumny w klauzuli SELECT polecenia SQL
        //Dla typu int uzywamy rs.getInt()
        int empno = rs.getInt("empno");
        //Dla typu string uzywamy rs.getString() - kolumna ename musi istniec w klauzuli SELECT
        String ename = rs.getString("ename");
        String job = rs.getString("job");
        int manager = rs.getInt("manager");
        //Dla typu date uzywamy rs.getDate()
        Date hiredate = rs.getDate("hiredate");
        //Dla typu BigDecimal (uzwyana bardza czesto do reprezentacji pieniedzy) uzywamy rs.getBigDecimal()
        BigDecimal salary = rs.getBigDecimal("salary");
        BigDecimal commision = rs.getBigDecimal("commision");
        int deptno = rs.getInt("deptno");
        
         return new Employee(empno, ename, job, manager, hiredate, salary, commision, deptno);

    }

    @Override
    public void create(Employee employee) throws Exception {
        try (Connection conn = jdbcConnectionManager.getConnection()){
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

    @Override
    public void update(Employee employee) throws Exception {
        try (Connection conn = jdbcConnectionManager.getConnection()){
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
    public void delete(int id) throws Exception {
        try(Connection conn = jdbcConnectionManager.getConnection()) {
            PreparedStatement ps = conn.prepareStatement(DELETE_STMT);
            ps.setInt(1, id);

            int numberOfAffectedRows = ps.executeUpdate();

            System.out.println("EmpDAO.delete() number of affected rows: " + numberOfAffectedRows);
        }
    }

    /*@Override
    public void create(List<Employee> employees) throws Exception {
        try(Connection conn = jdbcConnectionManager.getConnection()) {
        	conn.setAutoCommit(false);
        	
        	for(Employee employee: employees){
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
        	conn.commit();
        }
    }*/

    
    @Override
    public void create(List<Employee> employees) throws Exception {
        try(Connection conn = jdbcConnectionManager.getConnection()) {
        	conn.setAutoCommit(false);
        	
        	PreparedStatement ps = conn.prepareStatement(INSERT_STMT);
        	
        	for(Employee employee: employees){
                ps.setInt(1, employee.getEmpno());
                ps.setString(2, employee.getEname());
                ps.setString(3, employee.getJob());
                ps.setInt(4, employee.getManager());
                ps.setDate(5, new Date(employee.getHiredate().getTime()));            
                ps.setBigDecimal(6, employee.getSalary());
                ps.setBigDecimal(7, employee.getCommision());
                ps.setInt(8, employee.getDeptno());
                ps.addBatch();
        	}
            int numberOfAffectedRows[] = ps.executeBatch();

            int numberOfAffectedRowsTotal = Arrays.stream(numberOfAffectedRows).sum();
            System.out.println("EmpDAO.create() number of affected rows: " + numberOfAffectedRowsTotal);       
	
        	conn.commit();
        }
    }
    
    @Override
    public BigDecimal getTotalSalaryByDept(int dept) throws Exception {
        try (Connection conn = jdbcConnectionManager.getConnection()){
            CallableStatement ps = conn.prepareCall(QUERY_TOTAL_SALARY_BY_DEPT);
            
            ps.registerOutParameter(1, java.sql.Types.NUMERIC);
            ps.setInt(2, dept);
            
            ps.execute();
            
            BigDecimal salary = ps.getBigDecimal(1);
            
            return salary;
        }
    }
}
