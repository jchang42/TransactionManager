package com.transaction.project.model;

public class Transaction {
	
	private Account account;
	private Merchant merchant;
	private TransactionDetails details;
	
	public Transaction() {}
	
	public Transaction(Account account, Merchant merchant, TransactionDetails details) {
		this.account = account;
		this.merchant = merchant;
		this.details = details;
	}
	
	public Account getAccount() {
		return account;
	}
	
	public Merchant getMerchant() {
		return merchant;
	}
	
	public TransactionDetails getTransactionDetails() {
		return details;
	}
}
