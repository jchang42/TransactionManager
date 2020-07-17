class Account {
	accountNumber: string;
	firstName: string;
	lastName: string;

	constructor(res) {
		this.accountNumber = res.accountNumber;
		this.firstName = res.firstName;
		this.lastName = res.lastName;
	}
}

class Merchant {
	merchantName: string;
	issuer: string;
	acquirer: string;

	constructor(res) {
		this.merchantName = res.merchantName;
		this.issuer = res.issuer;
		this.acquirer = res.acquirer;
	}
}

class TransactionDetails {
	date: string;
	status: boolean;
	statusMessage: string;
	value: number;

	constructor(res) {
		this.date = res.date;
		this.status = res.status;
		this.statusMessage = res.statusMessage;
		this.value = res.value;
	}
}

export class Transaction {
	account: Account;
	merchant: Merchant;
	details: TransactionDetails;

	constructor(res) {
		this.account = new Account(res.account);
		this.merchant = new Merchant(res.merchant);
		this.details = new TransactionDetails(res.transactionDetails);
	}
}