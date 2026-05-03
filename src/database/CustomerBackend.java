package database;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class CustomerBackend 
{
	Connection conn;
	PreparedStatement p;
	ResultSet r;
	int custID;
	public CustomerBackend(Connection conn, int custID)
	{
		this.conn = conn;
		this.custID = custID;
	}
	
	public void categoryList(ArrayList<String> s)
	{
		try
		{
			String maker;
			p = conn.prepareStatement("select type from category");
			r = p.executeQuery();
			while ( r.next() )
			{
				maker = r.getString(1);
				s.add(maker);
			}
		}
		catch(SQLException e)
		{
			
		}
	}
	public void fetchBillers(ArrayList<String> billerCategory, ArrayList<ArrayList<String>> billerList) 
	{
		try
		{
			for ( int i = 0 ; i < billerCategory.size() ; i++ )
			{
				billerList.add(new ArrayList<>());
				String another = "select name from biller where category_id = ( select id from category where type = ? )";
				p = conn.prepareStatement(another);
				p.setString(1, billerCategory.get(i));
				r = p.executeQuery();
				while ( r.next() )
				{
					billerList.get(i).add(r.getString(1));
				}
			}
		}
		catch(SQLException e)
		{
			throw new RuntimeException("SQL Error in fetchBillers", e);
		}
	}
	
	public int addUserBiller(String name, String reference_number)
	{
		try
		{
			System.out.println("addUserBiller called with custID: " + custID + ", name: " + name + ", reference: " + reference_number);

			if (custID <= 0) {
				System.out.println("Error: Invalid custID: " + custID);
				return -1;
			}

			// Check if biller already exists
			String fetch = "select id from users_biller where cust_id = ? and name = ? and reference_number = ?";
			p = conn.prepareStatement(fetch);
			p.setInt(1, custID);
			p.setString(2, name);
			p.setString(3, reference_number);

			r = p.executeQuery();

			int count = 0;
			while ( r.next() )
			{
				count++;
			}

			if ( count != 0 )
			{
				System.out.println("Biller already exists for customer");
				return 0;
			}

			// Insert new biller
			p = conn.prepareStatement("insert into users_biller(name,cust_id,reference_number) values (?,?,?)");
			p.setString(1, name);
			p.setInt(2, custID);
			p.setString(3, reference_number);

			p.execute();
			System.out.println("Biller added successfully");
		}
		catch(SQLException e)
		{
			throw new RuntimeException("SQL Error in addUserBiller", e);
		}

		return 1;
	}
	
	public int verifyPayeeExistence(int accNum)
	{
		try
		{
			String verify = "select number from account where number = ?";
			p = conn.prepareStatement(verify);
			p.setInt(1, accNum);
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
			throw new RuntimeException("SQL Error in verifyPayeeExistence", e);
		}
		return 1;
	}
	
	public int addNewPayee(int accNum, String name )
	{
		try
		{
			System.out.println("addNewPayee called with custID: " + custID + ", accNum: " + accNum + ", name: " + name);

			if (custID <= 0) {
				System.out.println("Error: Invalid custID: " + custID);
				return -1;
			}

			// Check if payee already exists
			String fetch = "select custid from payee where Account = ? and name = ?";
			p = conn.prepareStatement(fetch);
			p.setInt(1, accNum);
			p.setString(2, name);
			r = p.executeQuery();

			int count = 0 ;
			while ( r.next() )
			{
				count++;
			}
			if ( count != 0 )
			{
				System.out.println("Payee already exists");
				return 0;
			}

			// Insert new payee
			p = conn.prepareStatement("insert into payee(Account,name,custid) values (?,?,?)");
			p.setInt(1, accNum);
			p.setString(2, name);
			p.setInt(3, custID);
			p.execute();
			System.out.println("Payee added successfully");
		}
		catch(SQLException e)
		{
			throw new RuntimeException("SQL Error in addNewPayee", e);
		}
		return 1;
	}
	
	public int fetchUsersBillers(ArrayList<String> billerName, ArrayList<String> referenceNumber)
	{
		try
		{
			String data = "select name,reference_number from users_biller where cust_id = ?";
			p = conn.prepareStatement(data);
			p.setInt(1, custID);
			r = p.executeQuery();
			
			while ( r.next() )
			{
				billerName.add(r.getString(1));
				referenceNumber.add(r.getString(2));
			}
		}
		catch(SQLException e)
		{
			throw new RuntimeException("SQL Error in fetchUsersBillers", e);
		}
		return 1;
	}
	
	public int fetchUsersPayee(ArrayList<String> PayeeName, ArrayList<String> accountID)
	{
		try
		{
			String data = "select name,Account from Payee where custid = ?";
			p = conn.prepareStatement(data);
			p.setInt(1, custID);
			r = p.executeQuery();

			while ( r.next() )
			{
				PayeeName.add(r.getString(1));
				accountID.add(r.getString(2));
			}
		}
		catch(SQLException e)
		{
			throw new RuntimeException("SQL Error in fetchUsersPayee", e);
		}
		return 1;
	}
	
	public int addTransaction(String senderAccNum, String receiverAccNum, String amount )
	{
		try
		{
			String fetch = "UPDATE account set balance = round( balance - ?,4) where number = ?";
			p = conn.prepareStatement(fetch);
			p.setDouble(1, Double.parseDouble(amount));
			p.setInt(2, Integer.parseInt(senderAccNum));

			String fetch1 = "UPDATE account set balance = round( balance + ?,4) where number = ?";
			PreparedStatement p1 = conn.prepareStatement(fetch1);
			p1.setDouble(1, Double.parseDouble(amount));
			p1.setInt(2, Integer.parseInt(receiverAccNum));

			p.execute();
			p1.execute();

			p = conn.prepareStatement("insert into transaction(credit,debit,amount) values (?,?,?)");
			p.setInt(1, Integer.parseInt(senderAccNum));
			p.setInt(2, Integer.parseInt(receiverAccNum));
			p.setDouble(3, Double.parseDouble(amount));

			p.execute();

			return 1;
		}
		catch(SQLException e)
		{
			throw new RuntimeException("SQL Error in addTransaction", e);
		}
	}
	
	public int addBillPayment(String arguments)
	{
		String[] args = arguments.split("\n");
		try
		{
			//Subtract the amount from user account
			String fetch = "UPDATE account set balance = round(?,4) where number = ?";
			p =conn.prepareStatement(fetch);
			p.setDouble(1, Double.parseDouble(args[4]) - Double.parseDouble(args[3]));
			p.setInt(2, Integer.parseInt(args[2]));
			p.execute();

			//Add the record to billpayment
			String getId = "select id from users_biller where cust_id = ? and name = ? and reference_number = ?";
			p = conn.prepareStatement(getId);
			p.setInt(1, custID);
			p.setString(2, args[0]);
			p.setString(3, args[1]);

			r = p.executeQuery();

			int bill_id = -1;
			while ( r.next() )
			{
				bill_id = r.getInt(1);
			}
			System.out.println("Bill id : " + bill_id);

			p = conn.prepareStatement("insert into billpayment(bill_id,amount,acc) values (?,?,?)");
			p.setInt(1, bill_id);
			p.setDouble(2, Double.parseDouble(args[3]));
			p.setInt(3, Integer.parseInt(args[2]));
			p.execute();
		}
		catch(SQLException e)
		{
			throw new RuntimeException("SQL Error in addBillPayment", e);
		}
		return 1;
	}
	
	public double TodayTransaction(int accNum)
	{
		try
		{
			String fetch = "select sum(amount) from transaction where credit = ? and DATEPAYMENT >= CURDATE() AND DATEPAYMENT < CURDATE() + INTERVAL 1 DAY";
			p = conn.prepareStatement(fetch);
			p.setInt(1, accNum);

			ResultSet temp = p.executeQuery();

			double transamount = 0;
			while ( temp.next() )
			{
				transamount += temp.getDouble(1);
			}
			return transamount;
		}
		catch (SQLException e)
		{
			throw new RuntimeException("SQL Error in TodayTransaction", e);
		}
	}
	
	public void FetchUserAccountandLimits(ArrayList<Integer> accNum, ArrayList<Double> Limit)
	{
		try
		{
			String fetch = "select number,lim,status from account where custid = ?";
			p = conn.prepareStatement(fetch);
			p.setInt(1, custID);

			r = p.executeQuery();

			while ( r.next() )
			{
				String status = r.getString(3);
				if ( status.equals("frozen") || status.equals("deleted"))
				{
					continue;
				}
				int acc = r.getInt(1);
				accNum.add(acc);

				Limit.add(r.getDouble(2) - TodayTransaction(acc));
			}
		}
		catch (SQLException e)
		{
			throw new RuntimeException("SQL Error in FetchUserAccountandLimits", e);
		}
	}
	
	public void FetchUserAccount(ArrayList<String> accTitle, ArrayList<Integer> accNum, ArrayList<Double> accBalance)
	{
		accTitle.clear();
		accNum.clear();
		accBalance.clear();
		try
		{
			String fetch = "select number,balance,status,owner from account where custid = ?";
			p = conn.prepareStatement(fetch);
			p.setInt(1, custID);

			r = p.executeQuery();

			while ( r.next() )
			{
				String status = r.getString(3);
				if ( status.equals("frozen") || status.equals("deleted"))
				{
					continue;
				}
				accTitle.add(r.getString(4));
				accNum.add(r.getInt(1));
				accBalance.add(r.getDouble(2));
			}
		}
		catch (SQLException e)
		{
			throw new RuntimeException("SQL Error in FetchUserAccount", e);
		}
	}

	public void writeCsv(String filePath, int accNum, double closing_amount) 
	 {
		   	double amount = 0;
			FileWriter fileWriter = null;
			try {
			   fileWriter = new FileWriter(filePath);
			   
			   fileWriter.append("Account Number : " + "\t" + accNum + "\n");
			   //Start date
			   try
			   {
				   String fetch = "SELECT CURDATE() - INTERVAL 1 WEEK";
				   p =conn.prepareStatement(fetch);
				   
				   r = p.executeQuery();
				   
				   while ( r.next())
				   {
					   fileWriter.append("Start Date : " + "\t" + r.getString(1)+ "\n");
				   }
			   }
			   catch(SQLException e)
			   {
				   System.out.println("Here1");
				   System.exit(-1);
			   }
			   //End date
			   try
			   {
				   String fetch = "SELECT CURDATE()";
				   p =conn.prepareStatement(fetch);
				   
				   r = p.executeQuery();
				   
				   while ( r.next())
				   {
					   fileWriter.append("End Date : " + "\t" + r.getString(1) + "\n\n");
				   }
			   }
			   catch(SQLException e)
			   {
				   System.out.println("Here2");
				   System.exit(-1);
			   }
			   
			   //Adding the transaction column
			   fileWriter.append("Transaction #\tType\tAmount\tTo/From\tDateTime\n");
			   //Adding the transaction data using query from mysql
			   try
			   {
				   String fetch = "SELECT * FROM TRANSACTION WHERE CREDIT = " + accNum + " or DEBIT = " + accNum;
				   p =conn.prepareStatement(fetch);
				   
				   r = p.executeQuery();
				   int count = 1;
				   int credit;
				   int debit;
				   while ( r.next())
				   {
					   //Iterator
					   fileWriter.append(Integer.toString(count));
					   fileWriter.append("\t");
					   
					   credit = r.getInt(1);
					   debit = r.getInt(2);
					   double current_amount = r.getDouble(4);
					   //Type
					   if ( credit == accNum )
					   {
						   fileWriter.append("Credit");
						   fileWriter.append("\t");
						   amount += current_amount;
					   }
					   else
					   {
						   fileWriter.append("Debit");
						   fileWriter.append("\t");
						   amount -= current_amount;
					   }
					   //Amount
					   fileWriter.append(Double.toString(current_amount));
					   fileWriter.append("\t");
					   //to/From
					   if ( credit == accNum )
					   {
						   fileWriter.append(Integer.toString(debit));
						   fileWriter.append("\t");
					   }
					   else
					   {
						   fileWriter.append(Integer.toString(credit));
						   fileWriter.append("\t");
					   }
					   //DateTime
					   fileWriter.append(r.getString(3));
					   fileWriter.append("\n");
					   count++;
				   }
			   }
			   catch(SQLException e)
			   {
				   System.out.println("Here3");
				   System.exit(-1);
			   }
			   fileWriter.append("\n\n");
			   
			   //Adding the bill column
			   fileWriter.append("BillPayment #\tBiller\tReference No.\tAmount\tDateTime\n");
			   //Adding the bill payment data using query from mysql
			   try
			   {
				   String fetch = "SELECT * FROM billpayment WHERE acc = " + accNum;
				   p =conn.prepareStatement(fetch);
				   
				   r = p.executeQuery();
				   int count = 1;
				   while ( r.next())
				   {
					   //Iterator
					   fileWriter.append(Integer.toString(count));
					   fileWriter.append("\t");
					   
					   //Name and reference
					   PreparedStatement p_temp;
					   ResultSet r_temp;
					   String fetch_billers = "select name,reference_number from USERS_BILLER where id = " + r.getInt(1);
					   p_temp = conn.prepareStatement(fetch_billers);
					   
					   r_temp = p_temp.executeQuery();
					   
					   while ( r_temp.next() )
					   {
						 //Name
						 fileWriter.append(r_temp.getString(1));
						 fileWriter.append("\t");
						 //Reference
						 fileWriter.append(r_temp.getString(2));
						 fileWriter.append("\t");
					   }
					   
					   
					   //Amount
					   double current_amount = r.getDouble(3);
					   fileWriter.append(Double.toString(current_amount));
					   amount += current_amount; 
					   fileWriter.append("\t");
					   //DateTime
					   fileWriter.append(r.getString(2));
					   fileWriter.append("\n");
					   count++;
				   }
			   }
			   catch(SQLException e)
			   {
				   System.out.println("Here4");
				   System.exit(-1);
			   }
			   fileWriter.append("\n\n");
			   
			   //Opening and Closing Balance
			   fileWriter.append("Opening Balance\t" + Double.toString(closing_amount + amount)+"\n");
			   fileWriter.append("Closing Balance\t" + Double.toString(closing_amount)+"\n");
			   
			  } catch (IOException ex) {
			System.out.println("Here5");
			   System.err.println("Exception: " + ex.getMessage());
			   System.exit(-1);
			  } finally {
			   if (fileWriter != null) {
			     try {
			      fileWriter.flush();
			      fileWriter.close();
			     } catch (IOException e) {
			      System.err.println("Exception: " + e.getMessage());
			      System.exit(-1);
			     }
			   }
			  }
		 }
}
