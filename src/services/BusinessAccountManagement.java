package services;

import database.EstablishConnection;
import java.util.ArrayList;
import management.ManageAccount;

public class BusinessAccountManagement 
{
	//ACCOUNT
	ManageAccount mAccount;
	
	//CONNECTION TO THE DATABASE LAYER
	EstablishConnection connection;
	
	public BusinessAccountManagement(){
		connection = new EstablishConnection();
	}
	

	//MANAGE ACCOUNTS
	public ArrayList<String> mASearch(String query) {
		mAccount = new ManageAccount(connection.getConnection());
		int accountNumber = Integer.parseInt(query);
		int response = mAccount.VerifyAccount(accountNumber);
		
		return switch (response) {
			case 0 -> null;
			case -1 -> {
				System.out.println("Database Failed!");
				System.exit(-1);
				yield null;
			}
			default -> mAccount.AccountDetailsfromAccountNumber(accountNumber);
		};
	}

	//Search accounts by CustomerID
	public ArrayList<String> mASearchByCustomerID(String custID) {
		mAccount = new ManageAccount(connection.getConnection());
		return mAccount.AccountDetailsfromCustomerID(Integer.parseInt(custID));
	}

	public String mAAdd_CheckCustomer(String custID) {
		mAccount = new ManageAccount(connection.getConnection());
		
		
		String custName = mAccount.getCustomerName(Integer.parseInt(custID));
		
		if(custName.equals("-1"))
		{
			System.out.println("Database Failed!");
			System.exit(-1);
		}
		else
		{
			return custName;
		}
		
		return null;
	}
	
	//Actual insertion of Account
	public int mAAdd(String custID, String title, String type , String lim , String balance ) {
		mAccount = new ManageAccount(connection.getConnection());
		
		int response = mAccount.InsertInDataBase_newAccount(Integer.parseInt(custID), title, type, Integer.parseInt(lim), Double.parseDouble(balance));
		
		if(response == -1)
		{
			System.out.println("Database Failed!");
			System.exit(-1);
		}

		
		return response;
	}
	
	//Deletion of Account
	public int mADelete(String accNum) {
		mAccount = new ManageAccount(connection.getConnection());
		
		int response = mAccount.VerifyAccount(Integer.parseInt(accNum));

		if(response == -1)
		{
			System.out.println("Database Failed!");
			System.exit(-1);
		}
		else if(response == 0)
		{
			return 0;
		}
		
		response = mAccount.deleteAccountfromDatabase(accNum);
		
		if(response == -1)
		{
			System.out.println("Database Failed!");
			System.exit(-1);
		}
		
		return response;
	}
	
	//Modification of Account
	public int mAModify(String accNum, String title, String status, String type, String lim) {

		mAccount = new ManageAccount(connection.getConnection());
		
		int response = mAccount.VerifyAccount(Integer.parseInt(accNum));

		if(response == -1)
		{
			System.out.println("Database Failed!");
			System.exit(-1);
		}
		else if(response == 0)
		{
			return 0;
		}
		
		
		response = mAccount.modifyAccountfromDatabase(accNum, title, status, type, lim);
		
		if(response == -1)
		{
			System.out.println("Database Failed!");
			System.exit(-1);
		}
		
		return response;
	}
}
