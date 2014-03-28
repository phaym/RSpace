package com.carleton.paulhayman.RSpaceClient.models;

import javax.ws.rs.core.MediaType;

import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

public abstract class Transaction {
	
	protected String restPath = "";
	protected WebResource restSpace;
	protected ClientResponse webServiceResponse;
	protected int transId;
	
	/*A class which sends a tuple to RSpace RESTful Web Service*/
	public Transaction (WebResource restSpace, String path){
		this.restSpace = restSpace;
		this.restPath = path;
	}
	
	/*send the spaceEntry to received at specified RESTful endpoint*/
	protected ClientResponse callWebService(Object entry){
		ClientResponse webServiceResponse = restSpace.path(restPath).type(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.post(ClientResponse.class, entry);
		
		//check if response is good	
		if(webServiceResponse.getStatus() != 200){
			throw new RuntimeException("Failed : HTTP error code: " + webServiceResponse.getStatus());
		}
		
		return webServiceResponse;
	}

}
