package com.carleton.paulhayman.RSWebService.comm;


public class Response {
	
	public int transId;
	public String clientID;
	public String serializedTuple;
	public String className;
	
	public Response(int transId, String clientID){
		this.transId = transId;
		this.clientID = clientID;
	}

	public void setResult(String serializedTuple, String tupleClassName) {
		this.serializedTuple = serializedTuple;
		this.className = tupleClassName;
	}
	
	
	
}
