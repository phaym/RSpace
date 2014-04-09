package com.carleton.paulhayman.RSpaceClient.comm;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class TupleObject {
	
	public String name;
	public String serializedTuple;
	public String superClass;
	
	public TupleObject() {
	}
	
	public TupleObject(Object tupleObject) throws IllegalArgumentException, IllegalAccessException {
		
		//serialize object, include null fields so all fields are represented
		Gson gson = new GsonBuilder().serializeNulls().create();
		this.serializedTuple = gson.toJson(tupleObject);
		this.name = tupleObject.getClass().getCanonicalName();
		
		//set superclass, so matches can be found if it is a subclass of template
		if(tupleObject.getClass().getSuperclass() != null)
			this.superClass = tupleObject.getClass().getSuperclass().getCanonicalName();
	
	}

}
