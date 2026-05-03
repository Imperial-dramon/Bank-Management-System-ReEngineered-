package services;

import database.EstablishConnection;
import java.util.ArrayList;
import management.ManageCustomer;

public class BusinessCustomerManagment {
	//CUSTOMER
	ManageCustomer mCustomer;
	
	//CONNECTION TO THE DATABASE LAYER
	EstablishConnection connection;
	
	public BusinessCustomerManagment()
	{
		connection = new EstablishConnection();
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
}
