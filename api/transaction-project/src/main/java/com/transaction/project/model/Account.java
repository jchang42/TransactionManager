package com.transaction.project.model;

public class Account {

	private String accountNumber;
	
	private String firstName;
	private String lastName;
	
	public Account() {}
	
	public Account(String accountNumber, String firstName, String lastName) {
		this.accountNumber = accountNumber;
		this.firstName = firstName;
		this.lastName = lastName;
	}
	
	public String getAccountNumber() {
		return accountNumber;
	}
	
	public String getFirstName() {
		return firstName;
	}
	
	public String getLastName() {
		return lastName;
	}
}
