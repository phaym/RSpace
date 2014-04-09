package com.carleton.paulhayman.RSWebService.comm;

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

	public long getExpiryDate() {
		return expiryDate;
	}

	public String getSuperClassName() {
		return tuple.superClass;
	}
}
