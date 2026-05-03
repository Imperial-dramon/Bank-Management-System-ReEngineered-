package gui;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class CustomerLogin 
{
	Connection conn;
	PreparedStatement p;
	ResultSet r;
	
	public CustomerLogin(Connection conn)
	{
		this.conn = conn;
	}
	public int Customer_Login_Info(String username, String password)
	{
		try
		{
			String query = "select id from customer where Username = ? and Password = PASSWORD(?)";
			p = conn.prepareStatement(query);
			p.setString(1, username);
			p.setString(2, password);

			r = p.executeQuery();
			int count = 0;

			while ( r.next() )
			{
				count = r.getInt(1);
			}
			if ( count == 0 )
			{
				return 0;
			}
			return count;
		}
		catch(Exception e)
		{
			throw new RuntimeException("Customer Login Info Query Failed!", e);
		}
	}

	public boolean verifyCustomer(String user, String pass) {
    String sql = "SELECT * FROM customer WHERE Username = ? AND Password = PASSWORD(?)";

    System.out.println("Executing query: " + sql + " with user='" + user + "', pass length=" + pass.length());

    try (PreparedStatement pstmt = this.conn.prepareStatement(sql)) {
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
	
	public ArrayList<String> getUsernames() {
		ArrayList<String> usernames = new ArrayList<>();
		try {
			String query = "SELECT Username FROM customer";
			p = conn.prepareStatement(query);
			r = p.executeQuery();
			while (r.next()) {
				usernames.add(r.getString("Username"));
			}
		} catch (SQLException e) {
			throw new RuntimeException("Failed to fetch customer usernames", e);
		}
		return usernames;
	}
}
