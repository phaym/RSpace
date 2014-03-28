package com.carleton.paulhayman.RSpaceClient.dao;

import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.carleton.paulhayman.RSpaceClient.comm.Response;
import com.carleton.paulhayman.RSpaceClient.models.ResponsiveTransaction;

public class Responses {

	private static Responses instance;
	private static ConcurrentHashMap<Integer , ResponsiveTransaction> awaitingResults; // hashmap of transactionId, ResponsiveTransaction thread pairs
	
	/*DAO for future returned results from RSpace. Stores waiting threads, updates them
	 * with their results, allowing them to continue.
	 */
	private Responses(){
		
		awaitingResults = new ConcurrentHashMap<Integer, ResponsiveTransaction>();
	}
	
	/*add a new transactionId and waiting transaction pair*/
	public void addToWaitList(int key, ResponsiveTransaction value) throws InterruptedException{

		awaitingResults.put(key, value);
	}
	
	/*call from client endpoint which will provide the waiting thread with its result, allowing it to continue*/
	public void updateWaitList(Response response){
		
		ResponsiveTransaction waitingTransaction = awaitingResults.get(response.transId);
		
		if(waitingTransaction != null){
			waitingTransaction.provideTupleResult(response);
			awaitingResults.remove(response.transId);
		}		
		else{
			Logger.getLogger("RSpace").log(Level.WARNING, "Received response at current port for unknown transaction, previous client using this port may have terminated before response received.");
		}
		
	}

	public static Responses getInstance(){
		if(instance == null)
			instance = new Responses();
		return instance;
	}
}
