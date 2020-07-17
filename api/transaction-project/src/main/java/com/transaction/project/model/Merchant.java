package com.transaction.project.model;

public class Merchant {
	
	private String merchantName;
	private String acquirer;
	private String issuer;
	
	public Merchant() {}
	
	public Merchant(String merchantName, String acquirer, String issuer) {
		this.merchantName = merchantName;
		this.acquirer = acquirer;
		this.issuer = issuer;
	}
	
	public String getMerchantName() {
		return merchantName;
	}
	
	public String getAcquirer() {
		return acquirer;
	}
	
	public String getIssuer() {
		return issuer;
	}
}
