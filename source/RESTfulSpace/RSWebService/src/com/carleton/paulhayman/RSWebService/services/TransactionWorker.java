package com.carleton.paulhayman.RSWebService.services;

import java.util.concurrent.LinkedBlockingQueue;

import com.carleton.paulhayman.RSWebService.dao.ListenerQueue;
import com.carleton.paulhayman.RSWebService.dao.ResponseQueue;
import com.carleton.paulhayman.RSWebService.models.Transaction;
import com.carleton.paulhayman.RSWebService.models.WriteTransaction;

public class TransactionWorker implements Runnable {

	LinkedBlockingQueue<Transaction> queue;
	boolean running;
	
	public TransactionWorker(LinkedBlockingQueue<Transaction> queue){
		this.queue = queue;
		this.running = true;
	}
	
	@Override
	public void run() {
		
		Transaction spaceTransaction;
		boolean noFurtherAction;
		
		while (running){
			try {
				spaceTransaction = queue.take();
				if(spaceTransaction != null){		
					//if there is further action, it will be added to listener queue
					noFurtherAction = spaceTransaction.perform();
					
					if(noFurtherAction || spaceTransaction.isExpired()){
						addToResponseQueue(spaceTransaction);
					}
					else{
						addToListenerQueue(spaceTransaction);
					}
				}
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	//add to response queue
	public void addToResponseQueue(Transaction t){
		
		if(!(t instanceof WriteTransaction)){ //do nothing if a writetransaction enters this block
			ResponseQueue responseQueue = ResponseQueue.getInstance();
			responseQueue.addResponse(t.getResponse());
		}
	}
	
	//add to queue to wait for possible later response
	public void addToListenerQueue(Transaction t){
	
		ListenerQueue futureQueue = ListenerQueue.getInstance();
		futureQueue.addTransaction(t);
	}

	
}
