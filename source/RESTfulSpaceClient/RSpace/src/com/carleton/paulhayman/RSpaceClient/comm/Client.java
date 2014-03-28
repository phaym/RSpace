package com.carleton.paulhayman.RSpaceClient.comm;

public class Client {
	
	public String url;
	public long timeout;
	
	Client(){
	}
	
	public Client(String url, long timeout){
		this.url = url;
		this.timeout = timeout;
	}
}
