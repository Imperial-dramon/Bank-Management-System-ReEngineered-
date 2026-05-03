package gui;
import database.EstablishConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;


public class AccountantLogin {
	Connection conn;
	PreparedStatement p;
	ResultSet r;
	
	public AccountantLogin(Connection conn)
	{
		this.conn = conn;
	}
	public int Accountant_Login_Info(String username, String password)
	{
		try
		{
			String query = "select * from accountant where username = ? and password = PASSWORD(?)";
			p = conn.prepareStatement(query);
			p.setString(1, username);
			p.setString(2, password);

			r = p.executeQuery();
			int count = 0;

			while ( r.next() )
			{
				count++;
			}
			if ( count == 0 )
			{
				return 0;
			}
		}
		catch(SQLException e)
		{
			System.err.println("SQL Error in Accountant_Login_Info: " + e.getMessage());
			return -1;
		}
		return 1;
	}


	public boolean verifyAccountant(String user, String pass) {
    Connection dbConnection = new EstablishConnection().getConnection();
    String sql = "SELECT * FROM accountant WHERE username = ? AND password = PASSWORD(?)";

    System.out.println("Executing accountant query: " + sql + " with user='" + user + "', pass length=" + pass.length());

    try (PreparedStatement pstmt = dbConnection.prepareStatement(sql)) {
        pstmt.setString(1, user);
        pstmt.setString(2, pass);

        ResultSet rs = pstmt.executeQuery();
        boolean result = rs.next();
        System.out.println("Accountant query result: " + result);
        return result;
    } catch (SQLException e) {
        System.err.println("Accountant Login Query Failed: " + e.getMessage());
        return false;
    }
}
	
	public ArrayList<String> getUsernames() {
		ArrayList<String> usernames = new ArrayList<>();
		try {
			String query = "SELECT username FROM accountant";
			p = conn.prepareStatement(query);
			r = p.executeQuery();
			while (r.next()) {
				usernames.add(r.getString("username"));
			}
		} catch (SQLException e) {
			System.err.println("Get usernames failed: " + e.getMessage());
		}
		return usernames;
	}
}