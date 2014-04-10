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

@Path("read")
public class ReadFromSpace {
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response postReadTransaction(SpaceEntry entryToRead){	
		
		//create transaction and add to queue
		final int entryTransId = System.identityHashCode(entryToRead);
		FindMatchTransaction readTransaction = new FindMatchTransaction(entryToRead, entryTransId, false);
		TransactionQueue.getInstance().addTransaction(readTransaction);
		
		//return response with transaction ID for reference
		return Response.status(200).entity(entryTransId).build();	
	}
	
}
