package services;

import database.EstablishConnection;
import gui.ManagerLogin;
import java.util.ArrayList;
import management.ManageCustomer;
import management.Manages;

public class BusinessLayer {
	
	//MANAGER LOGIN
	ManagerLogin bmLogin;
	
	//MANAGEMENT PORTAL
	//CUSTOMER
	ManageCustomer mCustomer;
	//ACCOUNT
	Manages mAccount;

	
	//CONNECTION TO THE DATABASE LAYER
	EstablishConnection connection;
	
	public BusinessLayer(){
		connection = new EstablishConnection();
	}
	public int managerLogin(String inputUsername, char[] inputPassword) {
		bmLogin = new ManagerLogin(connection.getConnection());

		int response = bmLogin.Manager_Login_Info(
				inputUsername,
				new String(inputPassword));

		if(response == -1)
		{
			System.out.println("Database Layer Failed!");
			return -1;
		}

		return response;
	}
	//MANAGE CUSTOMERS
	public ArrayList<String> mCSearch(String criteria, String query){
		mCustomer = new ManageCustomer(connection.getConnection());
		switch(criteria) {
			case "CNIC":
				return mCustomer.CustomerinfoCNIC(query);
			case "ID":
				return mCustomer.CustomerinfoID(query);
			default:
				return null;
		}
	}

	public int mCModify(String custID, String address, String contact, String password) {
		mCustomer = new ManageCustomer(connection.getConnection());
		int response = 1;
		response &= mCustomer.changeAddress(address, Integer.parseInt(custID));
		response &= mCustomer.changeContact(contact, Integer.parseInt(custID));
		if(password.length() != 0)
		{
			response &= mCustomer.changePassword(password, Integer.parseInt(custID));
		}
		
		return response;
	}

	public int mCAdd(
	    		String name, String address, String contact,
	    		String CNIC, String username, String Password) {
		mCustomer = new ManageCustomer(connection.getConnection());
		
		return mCustomer.addCustomer(name, address, contact, CNIC, username, Password);
	}

	//MANAGE ACCOUNTS
	public ArrayList<String> mASearch(String query) {
		mAccount = new Manages(connection.getConnection());
		int response = mAccount.VerifyAccount(Integer.parseInt(query));

		switch(response) {
			case 0:
				return null;
			case -1:
				System.out.println("Database Failed!");
				return null;
			default:
				return mAccount.AccountDetailsfromAccountNumber(Integer.parseInt(query));
		}
	}

	//Search accounts by CustomerID
	public ArrayList<String> mASearchByCustomerID(String custID) {
		mAccount = new Manages(connection.getConnection());
		return mAccount.AccountDetailsfromCustomerID(Integer.parseInt(custID));
	}

	public String mAAdd_CheckCustomer(String custID) {
		mAccount = new Manages(connection.getConnection());

		String custName = mAccount.getCustomerName(Integer.parseInt(custID));

		if(custName.equals("-1"))
		{
			System.out.println("Database Failed!");
			return null;
		}
		else
		{
			return custName;
		}
	}
	
	//Actual insertion of Account
	public int mAAdd(String custID, String title, String type , String lim , String balance ) {
		mAccount = new Manages(connection.getConnection());

		int response = mAccount.InsertInDataBase_newAccount(Integer.parseInt(custID), title, type, Integer.parseInt(lim), Double.parseDouble(balance));

		if(response == -1)
		{
			System.out.println("Database Failed!");
			return -1;
		}


		return response;
	}
	
	//Deletion of Account
	public int mADelete(String accNum) {
		mAccount = new Manages(connection.getConnection());

		int response = mAccount.VerifyAccount(Integer.parseInt(accNum));

		switch(response) {
			case -1:
				System.out.println("Database Failed!");
				return -1;
			case 0:
				return 0;
		}

		response = mAccount.deleteAccountfromDatabase(accNum);

		if(response == -1)
		{
			System.out.println("Database Failed!");
			return -1;
		}

		return response;
	}
	
	//Modification of Account
	public int mAModify(String accNum, String title, String status, String type, String lim) {

		mAccount = new Manages(connection.getConnection());

		int response = mAccount.VerifyAccount(Integer.parseInt(accNum));

		switch(response) {
			case -1:
				System.out.println("Database Failed!");
				return -1;
			case 0:
				return 0;
		}

		response = mAccount.modifyAccountfromDatabase(accNum, title, status, type, lim);

		if(response == -1)
		{
			System.out.println("Database Failed!");
			return -1;
		}

		return response;
	}
}
