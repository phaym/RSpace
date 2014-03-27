package com.carleton.paulhayman.RSpaceClient.comm;


public class Response {
	
	public int transId;
	public String url;
	public String serializedTuple;
	public String className;
	
	public Response(){
	}
	
	public Response(int transId, String url, String tuple, String className){
		this.transId = transId;
		this.url = url;
		this.serializedTuple = tuple;
		this.className = className;
	}
}
