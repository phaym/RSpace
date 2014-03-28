package com.carleton.paulhayman.RSpaceClient.controller;

import java.net.URI;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import javax.ws.rs.core.UriBuilder;

import com.carleton.paulhayman.RSpaceClient.comm.Response;
import com.carleton.paulhayman.RSpaceClient.dao.Responses;
import com.carleton.paulhayman.RSpaceClient.models.ClientTransaction;
import com.carleton.paulhayman.RSpaceClient.models.InstantTransaction;
import com.carleton.paulhayman.RSpaceClient.models.ResponsiveTransaction;
import com.carleton.paulhayman.RSpaceClient.resources.ResponseService;
import com.carleton.paulhayman.RSpaceClient.services.ClientMonitor;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.api.json.JSONConfiguration;

public class RSpace {
	
	/*Server endpoint config*/
	private WebResource restSpace;
	private static String BASE_URL = "http://localhost:8080/RSWebService";

	/*server endpoints*/
	protected static String REST = "RSpace";
	protected static String READ = "read";
	protected static String TAKE = "take";
	protected static String WRITE = "write";
	protected static String READIFEXISTS = "readIfExists";
	protected static String TAKEIFEXISTS = "takeIfExists";
	protected static String REGISTER_CLIENT = "registerClient";
	protected static String RENEW_CLIENT = "renewClient";
	
	
	/*client endpoint config*/
	ResponseService responseService;
	public static String RETURN_URL = "";
	public static int DEFAULT_RETURN_PORT = 8182;
	public static String RETURN_ROUTER = "/client";
	public static String RETURN_RESOURCE = "/response";
	
	
	private static RSpace instance;
	private ExecutorService responsePool;
	public static String CLIENT_ID = "";
	protected ClientMonitor clientMonitor;
	
	private RSpace(int port) throws Exception {
		
		initializeRestSpace();
		initializeResponseService(port);
		generateClientID();
	}

	/*Initialize WebResource to make calls to server WebService*/
	private void initializeRestSpace() {
		
		ClientConfig config = new DefaultClientConfig();
		config.getFeatures().put(JSONConfiguration.FEATURE_POJO_MAPPING, Boolean.TRUE);
		Client client = Client.create(config);
		restSpace = client.resource(getBaseURI());
	}

	/*Initialize Restlet to receive future responses from server*/
	private void initializeResponseService(int port) throws Exception {
			//initialize Rest endpoint for client
			responseService = new ResponseService(port);
			responseService.init();	
			
			setReturnURL(port);	//build URL to send to server for response to client
			responsePool = Executors.newCachedThreadPool(); //create pool to launch threads waiting for response
			Responses.getInstance(); //initialize response queue
	}
	
	public void terminate(){
		try {
			responseService.terminate();
			responsePool.shutdownNow();
			clientMonitor.terminate();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/*set url of client endpoint*/
	private void setReturnURL(int port) {
		String responseURL = "http://" + responseService.returnClientURL()  + ":" + port;
		responseURL += RETURN_ROUTER + RETURN_RESOURCE;
		RETURN_URL = responseURL;
	}

	/*send tuple framework to WebService, returns matching tuple Object*/
	public Object read(Object tuple, long timeout){		
		 
		Response tupleResult = createResponsiveTransaction(tuple, timeout, REST, READ);
		return handleResult(tupleResult);
	}
	
	/*send tuple framework to WebService, returns matching tuple Object*/
	public Object take(Object tuple, long timeout){		
		 
		Response tupleResult = createResponsiveTransaction(tuple, timeout, REST, TAKE);
		return handleResult(tupleResult);
	}
	
	/*send tuple framework to WebService, returns matching tuple Object*/
	public Object readIfExists(Object tuple){		
		 
		Response tupleResult = createResponsiveTransaction(tuple, 0, REST, READIFEXISTS);
		return handleResult(tupleResult);
	}

	/*send tuple framework to WebService, returns matching tuple Object*/
	public Object takeIfExists(Object tuple){		
		 
		Response tupleResult = createResponsiveTransaction(tuple, 0, REST, TAKEIFEXISTS);
		return handleResult(tupleResult);
	}
	
	private void generateClientID() {
		
		ClientTransaction transaction = new ClientTransaction(restSpace.path(REST), REGISTER_CLIENT);
		String result =  transaction.registerClient(RETURN_URL);
		RSpace.CLIENT_ID = result;
		createClientMonitor(CLIENT_ID, RETURN_URL);
	}
	
	private void createClientMonitor(String clientID, String returnURL){
		
		clientMonitor = new ClientMonitor(clientID, returnURL, restSpace.path(REST), RENEW_CLIENT);
		clientMonitor.beginPolling();
	}

	/*write object to space, return timeout*/
	public Object write(Object tuple, long timeout){
		
	    createInstantTransaction(tuple, timeout, REST, WRITE);
		return timeout;
	}
	
	/*create a transaction that does not wait for a later response from the server */
	private Object createInstantTransaction(Object tuple, long timeout, String restPath, String actionEndpoint) {

		InstantTransaction transaction = new InstantTransaction(tuple, timeout, restSpace.path(restPath), actionEndpoint);
		return transaction.call();
	}
	
	/*create a transaction that will get a transaction ID from the server and wait for
	 the server to send a matching tuple response to the RESTful client endpoint for that transaction */
	private Response createResponsiveTransaction(Object tuple, long timeout, String restPath, String actionEndpoint){
		
		Callable<Object> futureTrans = new ResponsiveTransaction(tuple, timeout, restSpace.path(restPath), actionEndpoint);	
		Future<Object> spaceTask = this.responsePool.submit(futureTrans);
		
		//get result as serialized json, or null if no match
		Response tupleResult = null;
		try {
			tupleResult = (Response) spaceTask.get(); //launch thread that waits for response from server
			
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}
		return tupleResult;
	}
	
	private Object handleResult(Response tupleResult) {
		Object result = null;
		if(tupleResult != null && tupleResult.serializedTuple != null){
			try {
				result = new Gson().fromJson(tupleResult.serializedTuple, Class.forName(tupleResult.className));
			} catch (JsonSyntaxException | ClassNotFoundException e ) {
					e.printStackTrace();
			}
		}
		return result;
	}
	public static URI getBaseURI(){ 
		return UriBuilder.fromUri(BASE_URL).build();
	}
	
	public static RSpace getSpace(){
		return getSpace(DEFAULT_RETURN_PORT);
	}
	
	public static RSpace getSpace(int port){
		if(instance == null){
			try {
				instance = new RSpace(port);
			} catch (Exception e) {
				
				e.printStackTrace();
				return null;
			}
		}
		return instance;
	}
}
