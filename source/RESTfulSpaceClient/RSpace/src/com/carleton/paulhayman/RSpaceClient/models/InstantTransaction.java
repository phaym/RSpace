package com.carleton.paulhayman.RSpaceClient.models;

import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

public class InstantTransaction extends Transaction {

	public InstantTransaction(Object tuple, long timeout, WebResource restSpace, String path) {
		super(tuple, timeout, restSpace, path);
		// TODO Auto-generated constructor stub
	}
	
	public boolean call(){
		
		boolean successful = false;
		/*call web service and handle response*/
		ClientResponse response = callWebService();
		if( response.getStatus() == 200){
			successful = true;
		}
		
		return successful;
	}

}
