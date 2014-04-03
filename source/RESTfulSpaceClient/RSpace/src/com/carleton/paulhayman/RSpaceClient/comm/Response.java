package com.carleton.paulhayman.RSpaceClient.comm;


public class Response {
	
	private int transId;
	private String clientID;
	private String serializedTuple;
	private String className;
	
	public Response(){
	}

	public int getTransId() {
		return transId;
	}

	public String getClientID() {
		return clientID;
	}

	public String getSerializedTuple() {
		return serializedTuple;
	}

	public String getClassName() {
		return className;
	}
	
}
