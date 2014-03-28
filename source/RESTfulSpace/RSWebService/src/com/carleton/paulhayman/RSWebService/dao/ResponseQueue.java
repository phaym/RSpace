package com.carleton.paulhayman.RSWebService.dao;

import java.util.concurrent.LinkedBlockingQueue;

import org.restlet.engine.Engine;
import org.restlet.ext.jackson.JacksonConverter;

import com.carleton.paulhayman.RSWebService.models.Transaction;
import com.carleton.paulhayman.RSWebService.services.ResponseWorker;

public class ResponseQueue {
	
	private LinkedBlockingQueue<Transaction> responseQueue;
	
	private static ResponseQueue instance;
	private ResponseWorker worker;


	private ResponseQueue(){
		//necessary for Object to Json conversion with Restlet Post
		Engine.getInstance().getRegisteredConverters().add(new JacksonConverter());
		
		//intialize queue to place new transactions for space
		responseQueue = new LinkedBlockingQueue<Transaction>();
		worker = new ResponseWorker(responseQueue);
		
		//initialize worker to pick responses off the queue
		Thread workerThread = new Thread(worker);
		workerThread.start();
	}
	
	public LinkedBlockingQueue<Transaction> getResponseQueue(){
		return responseQueue;
	}
	
	public void addResponse(Transaction t){
		responseQueue.add(t);
	}
	
	public static ResponseQueue getInstance(){
		if(instance == null)
			instance = new ResponseQueue();
		return instance;
	}
}