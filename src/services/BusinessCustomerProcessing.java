package services;

import database.CustomerBackend;
import database.EstablishConnection;
import java.util.ArrayList;

public class BusinessCustomerProcessing {
	EstablishConnection connection;
	int custID;
	CustomerBackend customerMain;
	public BusinessCustomerProcessing()
	{
		connection = new EstablishConnection();
	}
	public void setCustID(int id)
	{
		custID = id;
	}
	public void fetchCustomerAccounts(ArrayList<String> accTitle, ArrayList<Integer> accNum, ArrayList<Double> accBalance) {
		customerMain = new CustomerBackend(connection.getConnection(), custID);
		
		customerMain.FetchUserAccount(accTitle, accNum, accBalance);
	}
	
	public void fetchBillers(ArrayList<String> billerCategory, ArrayList<ArrayList<String>> billerList) {
		customerMain = new CustomerBackend(connection.getConnection(), custID);
		
		billerCategory.clear();
		billerList.clear();
		
		customerMain.categoryList(billerCategory);
		customerMain.fetchBillers(billerCategory,billerList);
	}
	
	public int addUserBiller(String name, String reference_number)
	{
		customerMain = new CustomerBackend(connection.getConnection(), custID);
		
		int response = customerMain.addUserBiller(name, reference_number);
		
		if(response == -1)
		{
			System.out.println("Database Failed!");
			System.exit(-1);
		}
		
		return response;
	}
	
	public int addUserPayee(String Nickname, String accNum)
	{
		customerMain = new CustomerBackend(connection.getConnection(), custID);
		
		int response = customerMain.verifyPayeeExistence(Integer.parseInt(accNum));

		switch(response) {
			case -1:
				System.out.println("Database Failed!");
				System.exit(-1);
				break;
			case 0:
				return 0;
		}
		
		response = customerMain.addNewPayee(Integer.parseInt(accNum), Nickname);
		
		if(response == -1)
		{
			System.out.println("Database Failed! 0x0002");
			System.exit(-1);
		}
		
		return response;
	}

	public int fetchUsersBillers(ArrayList<String> billerName, ArrayList<String> referenceNumber) {
		customerMain = new CustomerBackend(connection.getConnection(), custID);
		
		int response = customerMain.fetchUsersBillers(billerName, referenceNumber);
		
		if(response == -1)
		{
			System.out.println("Database Failed!");
			System.exit(-1);
		}
		
		return response;
	}
	
	public int fetchUsersPayees(ArrayList<String> billerName, ArrayList<String> referenceNumber) {
		customerMain = new CustomerBackend(connection.getConnection(), custID);
		
		int response = customerMain.fetchUsersPayee(billerName, referenceNumber);
		
		if(response == -1)
		{
			System.out.println("Database Failed!");
			System.exit(-1);
		}
		
		return response;
	}
	
	public int addBillPayment(String billerName, String referenceNumber, String accNum, String amount, String accountBalance) {
		customerMain = new CustomerBackend(connection.getConnection(), custID);
		
		String arguments = billerName+"\n"+referenceNumber+"\n"+accNum+"\n"+amount+"\n"+accountBalance+"\n";
		
		int response = customerMain.addBillPayment(arguments);

		if(response == -1)
		{
			System.out.println("Database Failed!");
			System.exit(-1);
		}
		
		return response;
	}
	
	public void FetchUserAccountandLimits(ArrayList<Integer> accNum, ArrayList<Double> Limits) {
		customerMain = new CustomerBackend(connection.getConnection(), custID);
		
		accNum.clear();
		Limits.clear();
		
		customerMain.FetchUserAccountandLimits(accNum, Limits);
	}
	
	public int addTransaction(String senderAccNum, String receiverAccNum, String amount) {
		customerMain = new CustomerBackend(connection.getConnection(), custID);
		
		int response = customerMain.addTransaction(senderAccNum, receiverAccNum, amount);

		if(response == -1)
		{
			System.out.println("Database Failed!");
			System.exit(-1);
		}
		
		return response;
	}
	
	public void printStatement(String filePath, int accNum, double closing_amount) {
		customerMain = new CustomerBackend(connection.getConnection(), custID);
		
		customerMain.writeCsv(filePath, accNum, closing_amount);
	}
}
