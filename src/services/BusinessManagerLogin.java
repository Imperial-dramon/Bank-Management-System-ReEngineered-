package services;

import database.EstablishConnection;
import gui.ManagerLogin;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class BusinessManagerLogin {
    private ManagerLogin bmLogin;
    private final EstablishConnection connection;

    public BusinessManagerLogin() {
        connection = new EstablishConnection();
    }

    public int managerLogin(String inputUsername, char[] inputPassword) {
        bmLogin = new ManagerLogin(connection.getConnection());

        int response = bmLogin.Manager_Login_Info(
                inputUsername,
                new String(inputPassword));

        if (response == -1) {
            System.out.println("Database Layered Failed!");
            System.out.println("System will exit now...");
            System.exit(-1);
        }

        return response;
    }


    public boolean verifyLogin(String inputUser, String inputPass) {
    Connection conn = new EstablishConnection().getConnection();
    // Try lowercase first
    String sql = "SELECT * FROM manager WHERE username = ? AND password = PASSWORD(?)";

    System.out.println("Executing manager query: " + sql + " with user='" + inputUser + "', pass length=" + inputPass.length());

    try {
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setString(1, inputUser);
        pstmt.setString(2, inputPass);

        ResultSet rs = pstmt.executeQuery();
        boolean result = rs.next();
        System.out.println("Manager query result: " + result);

        // If lowercase fails, try uppercase
        if (!result) {
            sql = "SELECT * FROM manager WHERE Username = ? AND Password = PASSWORD(?)";
            System.out.println("Trying uppercase: " + sql);
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, inputUser);
            pstmt.setString(2, inputPass);
            rs = pstmt.executeQuery();
            result = rs.next();
            System.out.println("Manager query result (uppercase): " + result);
        }

        return result;

    } catch (SQLException e) {
        throw new RuntimeException("Manager Login Query Failed!", e);
    }
}
}
