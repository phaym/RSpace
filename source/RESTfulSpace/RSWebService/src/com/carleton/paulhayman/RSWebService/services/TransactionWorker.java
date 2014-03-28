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
				boolean noFurtherAction;
				//if there is further action, it will be added to listener queue
				noFurtherAction = newTransaction.perform();
				
				if(noFurtherAction || newTransaction.isExpired()){
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
		
		if(!(t instanceof WriteTransaction)){ //do nothing if a writetransaction enters this block
			ResponseQueue responseQueue = ResponseQueue.getInstance();
			responseQueue.addResponse(t);
		}
	}
	
	//add to queue to wait for possible later response
	protected void addToListenerQueue(Transaction t){
	
		ListenerQueue futureQueue = ListenerQueue.getInstance();
		futureQueue.addTransaction(t);
	}

	
}
