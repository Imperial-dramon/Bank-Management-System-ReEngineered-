package services;

import database.EstablishConnection;
import gui.CustomerLogin;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class BusinessCustomerLogin {
    private CustomerLogin custLogin;
     final EstablishConnection connection;

    public BusinessCustomerLogin() {
        connection = new EstablishConnection();
    }
    
    public int customerLogin(String inputUsername, char[] inputPassword,
                         ArrayList<String> customerTitles, ArrayList<String> customerAccounts,
                         ArrayList<Double> customerBalance, ArrayList<Integer> customerLimits) {

    if (connection.getConnection() == null) {
        System.out.println("Critical Error: Database connection is null.");
        return -1;
    }

    custLogin = new CustomerLogin(connection.getConnection());

    // First get customer ID
    int custId = -1;
    try {
        String getCustomerIdQuery = "SELECT id FROM customer WHERE Username = ?";
        PreparedStatement pstmt1 = connection.getConnection().prepareStatement(getCustomerIdQuery);
        pstmt1.setString(1, inputUsername);
        ResultSet rs1 = pstmt1.executeQuery();

        if (rs1.next()) {
            custId = rs1.getInt("id");
        }
    } catch (SQLException e) {
        throw new RuntimeException("Error fetching customer ID", e);
    }

    // Fetch customer account data
    try {
        if (custId != -1) {
            // Now fetch accounts using customer ID
            String query = "SELECT number, balance, lim, status FROM account WHERE custid = ?";
            PreparedStatement pstmt = connection.getConnection().prepareStatement(query);
            pstmt.setInt(1, custId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                String status = rs.getString("status");
                if (status.equals("frozen") || status.equals("deleted")) {
                    continue;
                }
                customerTitles.add("Account " + rs.getInt("number"));
                customerAccounts.add(rs.getString("number"));
                customerBalance.add(rs.getDouble("balance"));
                customerLimits.add(rs.getInt("lim"));
            }

            System.out.println("Fetched " + customerTitles.size() + " accounts for customer " + inputUsername);
        } else {
            System.out.println("Customer ID not found for username: " + inputUsername);
        }

    } catch (SQLException e) {
        throw new RuntimeException("Error fetching customer accounts", e);
    }

    return custId;
}


    public boolean verifyCustomer(String user, String pass) {
    Connection conn = new EstablishConnection().getConnection();
    String sql = "SELECT * FROM customer WHERE Username = ? AND Password = PASSWORD(?)";

    System.out.println("Executing query: " + sql + " with user='" + user + "', pass length=" + pass.length());

    try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
        pstmt.setString(1, user);
        pstmt.setString(2, pass);

        ResultSet rs = pstmt.executeQuery();
        boolean result = rs.next();
        System.out.println("Query result: " + result);
        return result;
    } catch (SQLException e) {
        throw new RuntimeException("Customer Login Query Failed!", e);
    }
}
}
