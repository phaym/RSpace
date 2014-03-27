package com.carleton.paulhayman.RSWebService.resources;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.carleton.paulhayman.RSWebService.comm.SpaceEntry;
import com.carleton.paulhayman.RSWebService.dao.TransactionQueue;
import com.carleton.paulhayman.RSWebService.models.WriteTransaction;

@Path("write")
public class WriteToSpace {
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response postWriteTransaction(SpaceEntry entryToWrite){	
		
		//create transaction and add to queue
		final int entryTransId = System.identityHashCode(entryToWrite);
		WriteTransaction writeTransaction = new WriteTransaction(entryToWrite, entryTransId);
		TransactionQueue.getInstance().addTransaction(writeTransaction);
		
		//return response
		return Response.status(200).build();	
	}
	
}
