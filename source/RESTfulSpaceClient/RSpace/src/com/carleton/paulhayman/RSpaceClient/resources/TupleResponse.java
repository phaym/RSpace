package com.carleton.paulhayman.RSpaceClient.resources;

import org.restlet.resource.Post;
import org.restlet.resource.ServerResource;

import com.carleton.paulhayman.RSpaceClient.comm.Response;
import com.carleton.paulhayman.RSpaceClient.dao.Responses;

public class TupleResponse extends ServerResource{

	@Post("json")
	public void tupleResponse(Response tupleResponse){
		
		Responses.getInstance().updateWaitList(tupleResponse);
	}
}
