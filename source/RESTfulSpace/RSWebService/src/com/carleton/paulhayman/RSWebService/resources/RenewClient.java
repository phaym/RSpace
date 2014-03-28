package com.carleton.paulhayman.RSWebService.resources;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.carleton.paulhayman.RSWebService.comm.Client;
import com.carleton.paulhayman.RSWebService.dao.MongoDbImpl;

@Path("renewClient")
public class RenewClient {
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response renewClient(Client clientInfo){		
		
		Response response = Response.status(200).build();
	    boolean success =  MongoDbImpl.getInstance().renewClient(clientInfo);
	    if(!success){
	    	response = Response.status(500).build();
	    }
		return response;	
	}
	
}