package com.carleton.paulhayman.RSpaceClient.models;

import com.carleton.paulhayman.RSpaceClient.comm.SpaceEntry;
import com.carleton.paulhayman.RSpaceClient.controller.RSpace;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

public class InstantTransaction extends Transaction {

	protected SpaceEntry spaceEntry;
	
	public InstantTransaction(Object tuple, long timeout, WebResource restSpace, String path) {
		super(restSpace, path);
		//create timeout date, set to max value if overflows to a negative number
		long expiryDate = System.currentTimeMillis() + timeout;
		expiryDate = (expiryDate < 0) ? Long.MAX_VALUE : expiryDate;
		//create an entry to be sent to endpoint
		try {
			spaceEntry = new SpaceEntry(tuple, expiryDate, RSpace.CLIENT_ID);
		} catch (IllegalArgumentException | IllegalAccessException e) {
			e.printStackTrace();
		}
	}
	
	public boolean call(){
		
		boolean successful = false;
		/*call web service and handle response*/
		ClientResponse response = callWebService(this.spaceEntry);
		if( response.getStatus() == 200){
			successful = true;
		}
		response.close();
		return successful;
	}

}
