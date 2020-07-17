package com.transaction.project.model;

public class TransactionDetails {
	
	private String date;
	private boolean status;
	private String statusMessage;
	private float value;
	
	public TransactionDetails() {}
	
	public TransactionDetails(String date, boolean status, String statusMessage, float value) {
		this.date = date;
		this.status = status;
		this.statusMessage = statusMessage;
		this.value = value;
	}
	
	public String getDate() {
		return date;
	}
	
	public boolean getStatus() {
		return status;
	}
	
	public String getStatusMessage() {
		return statusMessage;
	}
	
	public float getValue() {
		return value;
	}
}