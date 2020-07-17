import { Injectable } from '@angular/core';
import { Observable, of, throwError } from 'rxjs';
import { catchError, map, tap } from 'rxjs/operators';
import { HttpClient, HttpResponse, HttpErrorResponse } from '@angular/common/http';

import { Transaction } from './transaction';

@Injectable({
	providedIn: 'root',
})
export class TransactionService {
	private url = ``;

	constructor(private http: HttpClient) { }

	handleError(error: HttpErrorResponse) {
		let errorMessage = 'error';
		return throwError(errorMessage);
	}

	public getTransactions() {
		return this.http.get(`http://localhost:8083/accounts`);
	}
}