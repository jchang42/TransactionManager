package com.transaction.project;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.transaction.project.controller.TransactionController;

@SpringBootApplication
public class TransactionProjectApplication {
	public static void main(String[] args) {
		SpringApplication.run(TransactionProjectApplication.class, args);
		
		TransactionController.getSession();
	}

}
