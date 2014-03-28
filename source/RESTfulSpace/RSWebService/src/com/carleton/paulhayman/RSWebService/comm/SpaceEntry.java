package com.carleton.paulhayman.RSWebService.comm;

import java.util.HashMap;


public class SpaceEntry {

	public TupleObject tuple;
	public long expiryDate;
	public String clientID;
	
	public SpaceEntry(){
		
	}

	public String getClassName() {		
		return tuple.name;
	}

	public String getJSONSerializedTuple() {
		return tuple.serializedTuple;
	}

	public HashMap<String, Object> getFields() {
		return tuple.fields;
	}

	public long getExpiryDate() {
		return expiryDate;
	}

	public String getSuperClassName() {
		return tuple.superClass;
	}
}
