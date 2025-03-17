import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


class Student {
    private int studentID;
    private String name;
    private String department;
    private int marks;

    public Student(int studentID, String name, String department, int marks) {
        this.studentID = studentID;
        this.name = name;
        this.department = department;
        this.marks = marks;
    }

    public int getStudentID() { return studentID; }
    public String getName() { return name; }
    public String getDepartment() { return department; }
    public int getMarks() { return marks; }
}


class StudentDAO {
    private static final String URL = "jdbc:mysql://localhost:3306/testdb";
    private static final String USER = "root";
    private static final String PASSWORD = "password";

    public void addStudent(Student student) throws SQLException {
        String sql = "INSERT INTO Student (StudentID, Name, Department, Marks) VALUES (?, ?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, student.getStudentID());
            pstmt.setString(2, student.getName());
            pstmt.setString(3, student.getDepartment());
            pstmt.setInt(4, student.getMarks());
            pstmt.executeUpdate();
            System.out.println("Student added successfully!");
        }
    }

    public List<Student> getAllStudents() throws SQLException {
        List<Student> students = new ArrayList<>();
        String sql = "SELECT * FROM Student";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                students.add(new Student(rs.getInt("StudentID"), rs.getString("Name"),
                        rs.getString("Department"), rs.getInt("Marks")));
            }
        }
        return students;
    }

    public void updateStudentMarks(int studentID, int newMarks) throws SQLException {
        String sql = "UPDATE Student SET Marks = ? WHERE StudentID = ?";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, newMarks);
            pstmt.setInt(2, studentID);
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Student marks updated successfully!");
            } else {
                System.out.println("Student ID not found.");
            }
        }
    }

    public void deleteStudent(int studentID) throws SQLException {
        String sql = "DELETE FROM Student WHERE StudentID = ?";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, studentID);
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Student deleted successfully!");
            } else {
                System.out.println("Student ID not found.");
            }
        }
    }
}


public class StudentManagementSystem {
    public static void main(String[] args) {
        StudentDAO dao = new StudentDAO();
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\n1. Add Student\n2. View Students\n3. Update Marks\n4. Delete Student\n5. Exit");
            System.out.print("Enter choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            try {
                switch (choice) {
                    case 1 -> {
                        System.out.print("Enter Student ID: ");
                        int id = scanner.nextInt();
                        scanner.nextLine();
                        System.out.print("Enter Name: ");
                        String name = scanner.nextLine();
                        System.out.print("Enter Department: ");
                        String department = scanner.nextLine();
                        System.out.print("Enter Marks: ");
                        int marks = scanner.nextInt();

                        dao.addStudent(new Student(id, name, department, marks));
                    }
                    case 2 -> {
                        List<Student> students = dao.getAllStudents();
                        System.out.println("ID | Name | Department | Marks");
                        for (Student s : students) {
                            System.out.println(s.getStudentID() + " | " + s.getName() + " | " +
                                    s.getDepartment() + " | " + s.getMarks());
                        }
                    }
                    case 3 -> {
                        System.out.print("Enter Student ID to update marks: ");
                        int id = scanner.nextInt();
                        System.out.print("Enter new marks: ");
                        int marks = scanner.nextInt();
                        dao.updateStudentMarks(id, marks);
                    }
                    case 4 -> {
                        System.out.print("Enter Student ID to delete: ");
                        int id = scanner.nextInt();
                        dao.deleteStudent(id);
                    }
                    case 5 -> {
                        System.out.println("Exiting...");
                        scanner.close();
                        System.exit(0);
                    }
                    default -> System.out.println("Invalid choice.");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
