package com.transaction.project.controller;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import org.apache.spark.SparkConf;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.datastax.driver.core.Session;
import com.transaction.project.dao.CassandraAccess;
import com.transaction.project.model.Account;
import com.transaction.project.model.Merchant;
import com.transaction.project.model.Transaction;
import com.transaction.project.model.TransactionDetails;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;

@RestController
public class TransactionController {
	
	private static Session session;
	private static SparkSession spark;
	
	private static String appName;
	private static String sparkMaster;
	private static String sparkHost;
	private static String sparkPort;
	
	@Value("${app.name}")
	public void setAppName(String value) {
		appName = value;
	}
	
	@Value("${spark.master}")
	public void setSparkMaster(String value) {
		sparkMaster = value;
	}
	
	@Value("${spark.host}")
	public void setSparkHost(String value) {
		sparkHost = value;
	}
	
	@Value("${spark.port}")
	public void setSparkPort(String value) {
		sparkPort = value;
	}
	
	public static void getSession() {
		session = CassandraAccess.getInstance();
		SparkConf conf = new SparkConf().setAppName(appName).setMaster(sparkMaster);
		conf.set("spark.cassandra.connection.host", sparkHost);
		conf.set("spark.cassandra.connection.port", sparkPort);
		spark = SparkSession.builder().config(conf).getOrCreate();
		readTables();
	}
	
	private static void readTables() {
		HashMap<String, String> accountOptions = new HashMap<>();
		accountOptions.put("table", "accounts");
		accountOptions.put("keyspace", "manager");
		spark.read().format("org.apache.spark.sql.cassandra").options(accountOptions).load().createOrReplaceTempView("accounts");
		
		HashMap<String, String> merchantOptions = new HashMap<>();
		merchantOptions.put("table", "merchants");
		merchantOptions.put("keyspace", "manager");
		spark.read().format("org.apache.spark.sql.cassandra").options(merchantOptions).load().createOrReplaceTempView("merchants");
		
		HashMap<String, String> transactionOptions = new HashMap<>();
		transactionOptions.put("table", "transactions");
		transactionOptions.put("keyspace", "manager");
		spark.read().format("org.apache.spark.sql.cassandra").options(transactionOptions).load().createOrReplaceTempView("transactions");
	}

	@GetMapping("/transactions")
	public ResponseEntity<List<Transaction>> getTransactions(@RequestParam(value = "account", defaultValue = "") String accountNumber,
															 @RequestParam(value = "first", defaultValue = "") String firstName,
															 @RequestParam(value = "last", defaultValue = "") String lastName,
															 @RequestParam(value = "merchant", defaultValue = "") String merchantName,
															 @RequestParam(value = "acquirer", defaultValue = "") String acquirer,
															 @RequestParam(value = "issuer", defaultValue = "") String issuer) {
		String queryString = "SELECT * FROM ((transactions INNER JOIN accounts ON a_id = account_id) INNER JOIN merchants ON m_id = merchant_id)";
		
		HashMap<String, String> requestParams = new HashMap<>();
		requestParams.put("account_number", accountNumber);
		requestParams.put("first_name", firstName);
		requestParams.put("last_name", lastName);
		requestParams.put("merchant_name", merchantName);
		requestParams.put("acquirer", acquirer);
		requestParams.put("issuer", issuer);
		
		boolean emptyRequestParams = true;
		for (String paramValue: requestParams.values()) {
			if (!paramValue.isEmpty()) {
				queryString += " WHERE ";
				emptyRequestParams = false;
				break;
			}
		}
		
		if (!emptyRequestParams) {
			boolean multipleConditions = false;
			for (String param: requestParams.keySet()) {
				if (!requestParams.get(param).isEmpty()) {
					if (multipleConditions) {
						queryString += "AND ";
					} else {
						multipleConditions = true;
					}
					queryString += param + " = '" + requestParams.get(param) + "' ";
				}
			}
		}

		Dataset<Row> res = spark.sql(queryString);
		List<Row> rows = res.collectAsList();
		LinkedList<Transaction> transactions = new LinkedList<>();
		for (Row r: rows) {
			TransactionDetails details = new TransactionDetails(r.getTimestamp(0).toString(), r.getBoolean(3), r.getString(4), r.getFloat(5));
			Account account = new Account(r.getString(6), r.getString(7), r.getString(8));
			Merchant merchant = new Merchant(r.getString(10), r.getString(11), r.getString(12));
			Transaction transaction = new Transaction(account, merchant, details);
			transactions.add(transaction);
		}
		return new ResponseEntity<>(transactions, HttpStatus.OK);
	}
}