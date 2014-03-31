package com.carleton.paulhayman.RSWebService.services;

import java.util.concurrent.Phaser;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import com.carleton.paulhayman.RSWebService.dao.MongoDbImpl;
import com.carleton.paulhayman.RSWebService.dao.ResponseQueue;
import com.carleton.paulhayman.RSWebService.models.Transaction;

public class ListenerWorker implements Runnable {

	private Transaction transaction;
	private Phaser phaserLock;
	private boolean listening;
	
	public ListenerWorker(Transaction t, Phaser lock){
		
		this.transaction = t;
		this.listening = true;
		this.phaserLock = lock;
		this.phaserLock.register();
	}
	
	@Override
	public void run() {
		
		//don't perform transaction if the client is terminated
		while(listening && MongoDbImpl.getInstance().getClientURL(transaction.getClientID()) != null){
				
			boolean foundMatch = transaction.perform();
			
			//send found tuple back to client
			if(foundMatch || transaction.isExpired()){
				addToResponseQueue();
			}
			//wait for new write to occur
			else{
				try {
					int currentPhase = phaserLock.arrive();
					phaserLock.awaitAdvanceInterruptibly(currentPhase,  transaction.getMillisToTimeout(), TimeUnit.MILLISECONDS);
				} catch (InterruptedException | TimeoutException e) {
					//add to response queue on timeoutException
					addToResponseQueue();
				}
				catch(IllegalStateException e){
					System.out.println(" phase: " + this.phaserLock.getPhase() + " , parties : " + this.phaserLock.getArrivedParties());
				}
			}	
		}
	}

	//discontinue loop, deregister from barrier and add to response queue
	public void addToResponseQueue() {
	
		listening = false;
		try{
			phaserLock.arriveAndDeregister();
		}
		catch(IllegalStateException e){
			System.out.println(" phase: " + this.phaserLock.getPhase() + " , parties : " + this.phaserLock.getArrivedParties());
		}
		ResponseQueue responseQueue = ResponseQueue.getInstance();
		responseQueue.addResponse(this.transaction);	
	}

}
