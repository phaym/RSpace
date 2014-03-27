package com.carleton.paulhayman.RSWebService.dao;

import java.util.concurrent.ConcurrentHashMap;

import com.carleton.paulhayman.RSWebService.models.ListenerForClass;
import com.carleton.paulhayman.RSWebService.models.Transaction;
import com.carleton.paulhayman.RSWebService.models.WriteTransaction;

public class ListenerQueue {
	
	private ConcurrentHashMap<String, ListenerForClass> classListenerMap;
	private static ListenerQueue instance;

	/*Create map for queues. Waiting transactions can be notified by their own class or a subclass.
	 * When a new write transaction occurs it will notify threads waiting for its class and its superclass.
	 */
	public ListenerQueue(){
		
		classListenerMap = new ConcurrentHashMap<String, ListenerForClass>();
	}
	
	public void addTransaction(Transaction trans){
			
		/*if a new write has been created, notify the listener for appropriate classes*/
		if(trans.getClass().equals(WriteTransaction.class)){	
			notifyTransactions(trans);
		}	
		/*if this is a new read / take transaction, create a new listener*/
		else{		
			createWaitingTransaction(trans);
		}
	}
	
	public void notifyTransactions(Transaction trans){
		//use class and superclass so we can find matches by class or subclass
		String className = trans.getTupleEntry().getClassName();
		String superClassName = trans.getTupleEntry().getSuperClassName();
		
		/*notify super class*/
		if(classListenerMap.containsKey(superClassName)){
			classListenerMap.get(superClassName).triggerForClass();
		}
		/*notify class*/
		if(classListenerMap.containsKey(className)){
			classListenerMap.get(className).triggerForClass();
		}
	}
	
	public void createWaitingTransaction(Transaction trans){
		
		String className = trans.getTupleEntry().getClassName();
		ListenerForClass classListener = new ListenerForClass(className);	
		
		//add listener for class
		classListenerMap.putIfAbsent(className, classListener);	
		classListenerMap.get(className).addNewTransaction(trans); 
	}
	
	public static ListenerQueue getInstance(){
		if(instance == null)
			instance = new ListenerQueue();
		return instance;
	}
}

