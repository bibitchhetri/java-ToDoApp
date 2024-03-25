import java.sql.*;

public class JDBCTest {
    public static void main(String[] args) {
        try {
            // Load the MySQL JDBC driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Establish connection
            String jdbcUrl = "jdbc:mysql://localhost:3306/test";
            String username = "root";
            String password = "Bibit@123";
            Connection connection = DriverManager.getConnection(jdbcUrl, username, password);

            // Print connection success message
            System.out.println("JDBC connection successful.");

            // Close connection
            connection.close();
        } catch (ClassNotFoundException e) {
            System.out.println("MySQL JDBC driver not found.");
            e.printStackTrace();
        } catch (SQLException e) {
            System.out.println("Connection failed.");
            e.printStackTrace();
        }
    }
}

