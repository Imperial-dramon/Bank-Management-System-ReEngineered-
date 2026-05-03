package services;

import database.EstablishConnection;
import gui.AccountantLogin;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import models.Accountant;

public class BusinessAccountant {
	//ACCOUNTANT LOGIN
	AccountantLogin aLogin;
	Accountant aMain;
	
	//CONNECTION TO THE DATABASE LAYER
	EstablishConnection connection;
		
	public BusinessAccountant(){
			connection = new EstablishConnection();
	}
	public int accountantLogin(String inputUsername, char[] inputPassword) {
		aLogin = new AccountantLogin(connection.getConnection());
		
		int response = aLogin.Accountant_Login_Info(
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

	public int accountantCredit(String accNum, String amount) {
		try {
			aMain = new Accountant(connection.getConnection());
			
			int response = aMain.withDrawMoney(Integer.parseInt(accNum), Double.parseDouble(amount));
			
			if(response == -1)
			{
				System.out.println("Database Failed!");
				System.exit(-1);
			}
			else if(response == 0)
			{
				return response;
			}
			
			return 1;
		}
		catch (NumberFormatException e) {
			return 0;
		}
		
	}

	public int accountantDebit(String accNum, String amount) {
		try {
			aMain = new Accountant(connection.getConnection());
			
			int response = aMain.depositMoney(Integer.parseInt(accNum), Double.parseDouble(amount));
			
			if(response == -1)
			{
				System.out.println("Database Failed!");
				System.exit(-1);
			}
			
			return 1;
		}
		catch (Exception e) {
			return 0;
		}
	}


	public boolean verifyAccountant(String user, String pass) {
    Connection conn = new EstablishConnection().getConnection();
    // Querying the accountant table specifically
    String sql = "SELECT * FROM accountant WHERE username = ? AND password = PASSWORD(?)";
    
    try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
        pstmt.setString(1, user);
        pstmt.setString(2, pass);
        
        ResultSet rs = pstmt.executeQuery();
        return rs.next(); // Returns true if a record exists
    } catch (SQLException e) {
        System.err.println("Accountant Login Query Failed: " + e.getMessage());
        return false;
    }
}
}
