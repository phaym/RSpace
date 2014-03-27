package com.carleton.paulhayman.RSpaceClient.comm;


public class SpaceEntry {

	public TupleObject tuple;
	public long expiryDate;
	public String returnUrl;
	
	public SpaceEntry(Object tupleObject, long expiryDate, String returnUrl) throws IllegalArgumentException, IllegalAccessException{
		this.expiryDate = expiryDate;	
		this.returnUrl = returnUrl;
		this.tuple = new TupleObject(tupleObject);
	}
	
	
}
