package gui;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ManagerLogin 
{
	Connection conn;
	PreparedStatement p;
	ResultSet r;
	
	public ManagerLogin(Connection conn)
	{
		this.conn = conn;
	}
	public int Manager_Login_Info(String username, String password)
	{
		try
		{
			String query = "select * from manager where username = '" + username +"' and password = password('" + password + "')";
			p = conn.prepareStatement(query);
			
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
		catch(Exception e)
		{
			return -1;
		}
		return 1;
	}
	
	public ArrayList<String> getUsernames() {
		ArrayList<String> usernames = new ArrayList<>();
		try {
			String query = "SELECT username FROM manager";
			p = conn.prepareStatement(query);
			r = p.executeQuery();
			while (r.next()) {
				usernames.add(r.getString("username"));
			}
		} catch (SQLException e) {
		throw new RuntimeException("Failed to fetch manager usernames", e);
	}
		return usernames;
	}
}