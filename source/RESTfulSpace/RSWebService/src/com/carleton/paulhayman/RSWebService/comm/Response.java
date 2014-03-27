package com.carleton.paulhayman.RSWebService.comm;


public class Response {
	
	public int transId;
	public String url;
	public String serializedTuple;
	public String className;
	//tests
	
	public Response(int transId, String returnUrl){
		this.transId = transId;
		this.url = returnUrl;
	}

	public void setResult(String serializedTuple, String tupleClassName) {
		this.serializedTuple = serializedTuple;
		this.className = tupleClassName;
	}
	
	
	
}
