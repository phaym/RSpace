package com.carleton.paulhayman.RSWebService.comm;


public class Response {
	
	public int transId;
	public String serializedTuple;
	public String className;
	
	public Response(int transId){
		this.transId = transId;
	}

	public void setResult(String serializedTuple, String tupleClassName) {
		this.serializedTuple = serializedTuple;
		this.className = tupleClassName;
	}
	
	
	
}
