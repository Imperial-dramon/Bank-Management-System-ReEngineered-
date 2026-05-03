package gui;

import database.EstablishConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class BusinessCustomerLogin {
    private CustomerLogin custLogin;
    private final EstablishConnection connection;

    public BusinessCustomerLogin() {
        connection = new EstablishConnection();
    }
    
    public int customerLogin(String inputUsername, char[] inputPassword) {
        if (connection.getConnection() == null) {
            System.out.println("Critical Error: Database connection is null.");
            return -1;
        }

        custLogin = new CustomerLogin(connection.getConnection());
        
        int response = custLogin.Customer_Login_Info(
                inputUsername, 
                new String(inputPassword));
        
        if (response == -1) {
            System.out.println("Database Layer Failed!");
            System.out.println("System will exit now...");
            System.exit(-1);
        }
        
        return response;
    }

    public boolean verifyCustomer(String user, String pass) {
    Connection conn = new EstablishConnection().getConnection();
    // Double-check your column names in phpMyAdmin (Username vs username)
    String sql = "SELECT * FROM customer WHERE Username = ? AND password1 = ?";
    
    try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
        pstmt.setString(1, user);
        pstmt.setString(2, pass);
        
        ResultSet rs = pstmt.executeQuery();
        return rs.next();
    } catch (SQLException e) {
        throw new RuntimeException("Customer Login Query Failed!", e);
    }
}
}
