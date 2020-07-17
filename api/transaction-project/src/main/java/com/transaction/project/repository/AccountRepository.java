package com.transaction.project.repository;

import java.util.List;

import com.transaction.project.model.Account;

public interface AccountRepository {

	List<Account> findByAccountNumber(String accountNumber);

	List<Account> findByAttributes(String accountNumber, String firstName, String lastName);
}
