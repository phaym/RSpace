package com.carleton.paulhayman.RSpaceClient.comm;


public class SpaceEntry {

	public TupleObject tuple;
	public long expiryDate;
	public String clientID;
	
	public SpaceEntry(Object tupleObject, long expiryDate, String clientID) throws IllegalArgumentException, IllegalAccessException{
		this.expiryDate = expiryDate;	
		this.clientID = clientID;
		this.tuple = new TupleObject(tupleObject);
	}
	
	
}
