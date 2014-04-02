package com.carleton.paulhayman.RSpaceClient.resources;

import org.restlet.Component;
import org.restlet.data.Protocol;
import org.restlet.routing.VirtualHost;

import com.carleton.paulhayman.RSpaceClient.controller.RSpace;

public class ResponseService {
	
	Component component;

	
	public ResponseService(int port) {
	
		component = new Component();		
		component.getServers().add(Protocol.HTTP, port);
		component.getDefaultHost().attach(RSpace.RETURN_ROUTER, 
				new ResponseRouter());
	}
	
	public void init() throws Exception{
		component.start();
	}
	
	public void terminate() throws Exception{
		component.stop();
	}
	
	
	public String returnClientURL(){
		return VirtualHost.getLocalHostAddress();
	}

}
