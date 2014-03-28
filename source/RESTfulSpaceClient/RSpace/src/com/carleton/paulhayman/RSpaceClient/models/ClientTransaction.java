package com.carleton.paulhayman.RSpaceClient.models;

import com.carleton.paulhayman.RSpaceClient.comm.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

public class ClientTransaction extends Transaction {

	protected Client clientEntry;
	protected final int TIMEOUT = 30000;
	
	public ClientTransaction(String returnURL, WebResource restSpace, String path) {
		super( restSpace, path);
		
		long expiryDate = System.currentTimeMillis() + TIMEOUT;
		expiryDate = (expiryDate < 0) ? Long.MAX_VALUE : expiryDate;
		//create an entry to be sent to endpoint
		clientEntry = new Client(returnURL, expiryDate);
	}
	
	public String registerClient(){
		
		/*call web service and handle response*/
		ClientResponse response = callWebService(clientEntry);
		return response.getEntity(String.class);
	}

}
