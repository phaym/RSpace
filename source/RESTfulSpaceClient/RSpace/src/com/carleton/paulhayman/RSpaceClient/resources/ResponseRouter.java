package com.carleton.paulhayman.RSpaceClient.resources;

import org.restlet.Application;
import org.restlet.Restlet;
import org.restlet.engine.Engine;
import org.restlet.ext.jackson.JacksonConverter;
import org.restlet.routing.Router;

import com.carleton.paulhayman.RSpaceClient.controller.RSpace;

public class ResponseRouter extends Application{

	
	@Override
	public synchronized Restlet createInboundRoot(){
		Engine.getInstance().getRegisteredConverters().add(new JacksonConverter());
		Router router = new Router(getContext());
		
		router.attach(RSpace.RETURN_RESOURCE, TupleResponse.class);
		return router;
		
	}
}
