package com.carleton.paulhayman.RSpaceClient.models;

import com.carleton.paulhayman.RSpaceClient.comm.Client;
import com.carleton.paulhayman.RSpaceClient.services.ClientMonitor;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

public class ClientTransaction extends Transaction {
	
	public ClientTransaction( WebResource restSpace, String path) {
		super( restSpace, path);
	}
	
	public String registerClient(String returnURL){
		
		long expiryDate = System.currentTimeMillis() + ClientMonitor.TIMEOUT;
		expiryDate = (expiryDate < 0) ? Long.MAX_VALUE : expiryDate;
		//create an entry to be sent to endpoint
		Client clientEntry = new Client("",returnURL, expiryDate);
		ClientResponse response = callWebService(clientEntry);
		
		return response.getEntity(String.class);
	}

}
