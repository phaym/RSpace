package com.carleton.paulhayman.RSWebService.models;

import java.util.concurrent.Phaser;

import com.carleton.paulhayman.RSWebService.services.ListenerWorker;


public class ListenerForClass {
	
	protected String className;
	protected final Phaser phaserLock; 
	
	public ListenerForClass(String className){
		
		phaserLock = new Phaser(1);
		this.className = className;
	}

	public void addNewTransaction(Transaction t){
		
		//launch new thread to wait for matching tuple to be written
		new Thread(new ListenerWorker(t, phaserLock)).start();
	}
	
	public void triggerForClass(){
		
		phaserLock.arrive();
	}
	
}
