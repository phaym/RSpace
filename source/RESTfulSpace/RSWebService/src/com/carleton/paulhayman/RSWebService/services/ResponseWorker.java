package com.carleton.paulhayman.RSWebService.services;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.restlet.data.MediaType;
import org.restlet.resource.ClientResource;
import org.restlet.resource.ResourceException;

import com.carleton.paulhayman.RSWebService.comm.Response;
import com.carleton.paulhayman.RSWebService.dao.MongoDbImpl;

public class ResponseWorker implements Runnable {

	LinkedBlockingQueue<Response> queue;
	boolean running;
	
	/*waits for tuple matches to be added to its queue to be sent back to client*/
	public ResponseWorker(LinkedBlockingQueue<Response> queue){
		this.queue = queue;
		this.running = true;
	}
	
	@Override
	public void run() {
		
		Response spaceResponse;
		while (running){
			try {
				spaceResponse = queue.take();
				if(spaceResponse != null){
					sendResponse(spaceResponse);
				}
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	/*send response to client contained matched tuple, or null if none found*/
	private void sendResponse(Response spaceResponse) {
		String url = MongoDbImpl.getInstance().getClientURL(spaceResponse.clientID);
		if(url != null){
			ClientResource cr = new ClientResource(url);
			try {
				cr.post(spaceResponse, MediaType.APPLICATION_JSON); //attempt to post response to client endpoint
			}catch( ResourceException e){ //handle error if client endpoint not available
				
				Logger.getLogger("RSpace").log(Level.WARNING, "Connection not available with client endpoint, response not sent.");
			}
			cr.release();
		}
	}
	
}

