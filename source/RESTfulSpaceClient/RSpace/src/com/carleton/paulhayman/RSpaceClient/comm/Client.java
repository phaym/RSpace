package com.carleton.paulhayman.RSpaceClient.comm;

public class Client {
	
	public String clientID;
	public String url;
	public long timeout;
	
	public Client(){
	}
	
	public Client(String clientID, String url,long timeout){
		
		this.clientID = clientID;
		this.url = url;
		this.timeout = timeout;
	}
	
	
}
