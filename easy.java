import java.sql.*;

public class EmployeeDatabase {

    private static final String DB_URL = "jdbc:mysql://localhost:3306/your_database"; // Replace with your DB name
    private static final String USER = "your_username"; // Replace with your DB username
    private static final String PASSWORD = "your_password"; // Replace with your DB password

    public static void main(String[] args) {

        String query = "SELECT EmpID, Name, Salary FROM Employee";

        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASSWORD);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            System.out.println("EmpID\tName\t\tSalary");
            System.out.println("---------------------------------");

           
            while (rs.next()) {
                int empId = rs.getInt("EmpID");
                String name = rs.getString("Name");
                double salary = rs.getDouble("Salary");

                System.out.println(empId + "\t" + name + "\t\t" + salary);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
