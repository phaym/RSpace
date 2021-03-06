package com.carleton.paulhayman.RSWebService.resources;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.carleton.paulhayman.RSWebService.comm.SpaceEntry;
import com.carleton.paulhayman.RSWebService.dao.TransactionQueue;
import com.carleton.paulhayman.RSWebService.models.FindMatchTransaction;

@Path("take")
public class TakeFromSpace {
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response postTakeTransaction(SpaceEntry entryToRead){	
		
		//create transaction and add to queue
		final int entryTransId = System.identityHashCode(entryToRead);
		FindMatchTransaction takeTransaction = new FindMatchTransaction(entryToRead, entryTransId, true);
		TransactionQueue.getInstance().addTransaction(takeTransaction);
		
		//return response with transaction ID for reference
		return Response.status(200).entity(entryTransId).build();	
	}
	
}
