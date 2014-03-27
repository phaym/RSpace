package com.carleton.paulhayman.RSpaceClient.models;

import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.logging.Level;

import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.carleton.paulhayman.RSpaceClient.comm.Response;
import com.carleton.paulhayman.RSpaceClient.dao.Responses;
import com.carleton.paulhayman.RSpaceClient.resources.ResponseService;

public class ResponsiveTransaction extends Transaction implements Callable<Object> {

	private Response result;
	private CountDownLatch latch;
	
	/*a transaction with the RSpace that will provide to client TupleResponse endpoint
	 * the TupleObject result of an action when it is available.
	 */
	public ResponsiveTransaction(Object tuple, long timeout, WebResource restSpace, String path){
		super(tuple, timeout, restSpace, path);
		
		this.result = null;
		this.latch = new CountDownLatch(1); //a latch that will be notified when tuple result is provided to client web service
	}
	
	@Override
	public Response call() throws Exception {
		
		/*call web service and handle response*/
		ClientResponse response = callWebService();
		this.transId = (int) response.getEntity(Integer.class);
		
		/*wait for web service to provide tuple Object result*/
		awaitResult();
		
		/*return result*/
		return result;
	}
	
	/*wait for result provided by Responses class*/
	private void awaitResult(){	
		try {
			/*place thread/tuple identifier in waitlist for future responses from web service*/
			Responses.getInstance().addToWaitList(this.transId, this);
			this.latch.await();
			
		} catch (InterruptedException e) {

			ResponseService.getLogger().log(Level.WARNING, "Transaction #" + transId + " response listener was terminated");
		}
	}
	
	
	/*place tuple object result in thread and notify thread to continue*/
	public void provideTupleResult(Response response){
	
		this.result = response;
		this.latch.countDown();
	}
		
}

