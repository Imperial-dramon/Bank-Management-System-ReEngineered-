package gui;
import database.EstablishConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class BusinessManagerLogin 
{
	//MANAGER LOGIN
	ManagerLogin bmLogin;
	
	//CONNECTION TO THE DATABASE LAYER
	EstablishConnection connection;
		
	public BusinessManagerLogin(){
		connection = new EstablishConnection();
	}
	
	public int managerLogin(String inputUsername, char[] inputPassword) {
		bmLogin = new ManagerLogin(connection.getConnection());
		
		int response = bmLogin.Manager_Login_Info(
				inputUsername, 
				new String(inputPassword));
		
		if(response == -1)
		{
			System.out.println("Database Layered Failed!");
			System.out.println("System will exit now...");
			
			System.exit(-1);
		}
		
		return response;
	}

	public boolean verifyLogin(String inputUser, String inputPass) {
    // 1. Get the connection from your EstablishConnection class
    Connection conn = new EstablishConnection().getConnection();
    
    // 2. The SQL query (Matches the role's table)
    String sql = "SELECT * FROM manager WHERE username = ? AND password = ?";
    
    try {
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setString(1, inputUser);
        pstmt.setString(2, inputPass);
        
        ResultSet rs = pstmt.executeQuery();
        
        // 3. If rs.next() is true, it means a matching record was found!
        return rs.next(); 
        
    } catch (SQLException e) {
        throw new RuntimeException("Manager Login Query Failed!", e);
    }
}
}
