package com.carleton.paulhayman.RSpaceClient.services;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.carleton.paulhayman.RSpaceClient.comm.Client;
import com.carleton.paulhayman.RSpaceClient.models.Transaction;
import com.sun.jersey.api.client.WebResource;

public class ClientMonitor extends Transaction  {

	protected final String clientID;
	protected final String returnURL;
	protected boolean listening;
	private static long POLL_FREQUENCY = 30000;
	public static long TIMEOUT = POLL_FREQUENCY + 5000;
	ScheduledExecutorService pollSchedular;
	protected Runnable pollServerTask;
	
	public ClientMonitor( final String clientID, final String returnURL, WebResource restSpace, String path) {
		super( restSpace, path);
		
		this.clientID = clientID;
		this.returnURL = returnURL;
		pollSchedular = Executors.newScheduledThreadPool(1);
		
		/*create a task that will send a new timeout for client ID at REST endpoint*/
		pollServerTask = new Runnable(){
			@Override
			public void run() {
				long newTimeout = System.currentTimeMillis() + TIMEOUT;
				Client clientRenewal = new Client(clientID, returnURL, newTimeout);
				callWebService(clientRenewal);
			}
		};
	}
	
	/*schedual a task that will execute at the frequency given to update the timeout for client*/
	public void beginPolling(){
		
	     pollSchedular.scheduleAtFixedRate(pollServerTask, POLL_FREQUENCY, POLL_FREQUENCY, TimeUnit.MILLISECONDS);	
	}
	
	
	public void terminate() {
		listening = false;
		pollSchedular.shutdownNow();	
	}

}
