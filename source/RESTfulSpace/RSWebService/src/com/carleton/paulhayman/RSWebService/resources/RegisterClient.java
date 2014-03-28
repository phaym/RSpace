package com.carleton.paulhayman.RSWebService.resources;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.carleton.paulhayman.RSWebService.comm.Client;
import com.carleton.paulhayman.RSWebService.dao.MongoDbImpl;

@Path("registerClient")
public class RegisterClient {
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response registerClient(Client clientInfo){		
		
		String clientID = MongoDbImpl.getInstance().addClient(clientInfo);
		//return response with clientID
		return Response.status(200).entity(clientID).build();	
	}
	
}