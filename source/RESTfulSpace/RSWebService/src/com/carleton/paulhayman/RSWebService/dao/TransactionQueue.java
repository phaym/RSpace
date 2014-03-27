package com.carleton.paulhayman.RSWebService.dao;

import java.util.concurrent.LinkedBlockingQueue;

import com.carleton.paulhayman.RSWebService.models.Transaction;
import com.carleton.paulhayman.RSWebService.services.TransactionWorker;

public class TransactionQueue {
	
	private LinkedBlockingQueue<Transaction> transactions;
	
	private static TransactionQueue instance;
	private TransactionWorker worker;

	private TransactionQueue(){
		//intialize queue to place new transactions for space
		transactions = new LinkedBlockingQueue<Transaction>();	
		//initialize worker to pick transactions for space off the queue
		worker = new TransactionWorker(transactions);
		Thread workerThread = new Thread(worker);
		workerThread.start();
	}
	
	public LinkedBlockingQueue<Transaction> getTransactions(){
		return transactions;
	}
	
	public void addTransaction(Transaction t){
		transactions.add(t);
	}
	
	public static TransactionQueue getInstance(){
		if(instance == null)
			instance = new TransactionQueue();
		return instance;
	}
}
