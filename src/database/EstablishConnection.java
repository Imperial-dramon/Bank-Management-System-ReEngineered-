package database;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class EstablishConnection 
{
	Connection conn;
	
	public EstablishConnection()
	{
		// 1. Updated URL for newer drivers (sometimes requires timezone)
        String url = "jdbc:mysql://localhost:3306/bms?useSSL=false&serverTimezone=UTC";
        String username = "root";
        String password = "";
        
        try {
            // 2. Explicitly load the NEW driver class
            Class.forName("com.mysql.cj.jdbc.Driver"); 
            
            conn = DriverManager.getConnection(url, username, password);
            System.out.println("Connected Successfully to MySQL 9.6!");
        } catch (ClassNotFoundException ex) {
            System.err.println("MySQL Driver not found: " + ex.getMessage());
        } catch (SQLException ex) {
            System.err.println("Database connection failed: " + ex.getMessage());
            System.err.println("SQL State: " + ex.getSQLState());
            System.err.println("Error Code: " + ex.getErrorCode());
        }
	}
	public Connection getConnection()
	{
		return conn;
	}
}
