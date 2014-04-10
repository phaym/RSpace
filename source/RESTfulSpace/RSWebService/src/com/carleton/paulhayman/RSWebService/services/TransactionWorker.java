package com.carleton.paulhayman.RSWebService.services;

import java.util.concurrent.LinkedBlockingQueue;

import com.carleton.paulhayman.RSWebService.dao.ListenerQueue;
import com.carleton.paulhayman.RSWebService.dao.ResponseQueue;
import com.carleton.paulhayman.RSWebService.models.Transaction;

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
		
		while (running){
			try {
				spaceTransaction = queue.take();
				if(spaceTransaction != null){		
					processTransaction(spaceTransaction);
				}
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	/*create anonymous thread to process transaction*/
	protected void processTransaction(final Transaction newTransaction){
		Thread t = new Thread() {
		    public void run() {
				boolean sendResponse;
				//if match is found send response, if no match found or it is a write add to listener
				sendResponse = newTransaction.perform();
				
				if(sendResponse || newTransaction.isExpired()){
					addToResponseQueue(newTransaction);
				}
				else{
					addToListenerQueue(newTransaction);
				}
		    }
		};
		t.start();
	}

	//add to response queue
	protected void addToResponseQueue(Transaction t){
		
		ResponseQueue.getInstance().addResponse(t);
	}
	
	//add to queue to wait for possible later response
	protected void addToListenerQueue(Transaction t){
	
		ListenerQueue.getInstance().addTransaction(t);
	}

	
}
