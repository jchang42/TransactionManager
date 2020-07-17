import { Component, OnInit } from '@angular/core';
import { TransactionService } from './transaction.service';
import { Transaction } from './transaction';
import { HttpClient } from '@angular/common/http';
import { FormGroup, FormControl } from '@angular/forms';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit {
  title = 'manager';
  BASE_URL = "http://localhost:8083/transactions";
  transactions = [];
  totalPages = 0;
  page = 0;
  transactionsPerPage = 5;

  transactionForm = new FormGroup({
  	account: new FormControl(''),
  	first: new FormControl(''),
  	last: new FormControl(''),
  	merchant: new FormControl(''),
  	acquirer: new FormControl(''),
  	issuer: new FormControl('')
  });

  constructor(private http: HttpClient) {}

  ngOnInit(): void {
    this.getTransactions();
  }

  prevPage(): void {
  	if (this.page > 1) {
  		this.page--;
  	}
  }

  nextPage(): void {
  	if (this.page < this.totalPages) {
  		this.page++;
  	}
  }

  rowExpand(t): void {
    t.expanded = !t.expanded;
  }

  getTransactions(): void {
  	let url = this.BASE_URL;
  	let formValues = {};
  	let emptyReqParams = true;
  	for (const control in this.transactionForm.controls) {
  		formValues[control] = this.transactionForm.controls[control].value;
  		if (formValues[control] !== "") {
  			emptyReqParams = false;
  		}
  	}
  	if (!emptyReqParams) {
  		url += "?";
  		let multipleConditions = false;
  		for (const control in formValues) {
  			if (formValues[control] !== "") {
  				if (multipleConditions) {
  					url += "&";
  				} else {
  					multipleConditions = true;
  				}
  				url += control + "=" + formValues[control];
  			}
  		}
  	}
  	this.http.get<Transaction[]>(url).subscribe(data => {
  		this.transactions = data.map((res) => {
        return {
          transaction: new Transaction(res),
          expanded: false
        };
      });
      this.page = 1;
  		this.totalPages = Math.ceil(this.transactions.length / this.transactionsPerPage);
  	});
  }
}
