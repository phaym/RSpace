package com.carleton.paulhayman.RSpaceClient.models;

import javax.ws.rs.core.MediaType;

import com.carleton.paulhayman.RSpaceClient.comm.SpaceEntry;
import com.carleton.paulhayman.RSpaceClient.controller.RSpace;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

public abstract class Transaction {
	
	protected SpaceEntry spaceEntry = null;
	protected String restPath = "";
	protected WebResource restSpace;
	protected ClientResponse webServiceResponse;
	protected int transId;
	
	/*A class which sends a tuple to RSpace RESTful Web Service*/
	public Transaction(Object tuple, long timeout, WebResource restSpace, String path){
		this.restSpace = restSpace;
		this.restPath = path;
		
		//create timeout date, set to max value if overflows to a negative number
		long expiryDate = System.currentTimeMillis() + timeout;
		expiryDate = (expiryDate < 0) ? Long.MAX_VALUE : expiryDate;
	
		//create an entry to be sent to endpoint
		try {
			spaceEntry = new SpaceEntry(tuple, expiryDate, RSpace.RETURN_URL);
		} catch (IllegalArgumentException | IllegalAccessException e) {
			e.printStackTrace();
		}
	}
	
	/*send the spaceEntry to received at specified RESTful endpoint*/
	protected ClientResponse callWebService(){
		ClientResponse webServiceResponse = restSpace.path(restPath).type(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.post(ClientResponse.class, spaceEntry);
		
		//check if response is good	
		if(webServiceResponse.getStatus() != 200){
			throw new RuntimeException("Failed : HTTP error code: " + webServiceResponse.getStatus());
		}
		
		return webServiceResponse;
	}

}
