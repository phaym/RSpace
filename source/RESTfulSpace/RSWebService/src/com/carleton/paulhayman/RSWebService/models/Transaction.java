package com.carleton.paulhayman.RSWebService.models;

import com.carleton.paulhayman.RSWebService.comm.Response;
import com.carleton.paulhayman.RSWebService.comm.SpaceEntry;


public abstract class Transaction {
	
	protected final int transId;
	protected SpaceEntry tupleEntry;
	protected Response tupleResponse;

	public Transaction(SpaceEntry tupleEntry, int transId) {
		this.tupleEntry = tupleEntry;
		this.transId = transId;
		this.tupleResponse = new Response(this.transId, this.tupleEntry.clientID);
	}
	
	public SpaceEntry getTupleEntry() {
		return tupleEntry;
	}

	public final int getTransId(){
		return this.transId;
	}
	
	public boolean isExpired(){
		return (System.currentTimeMillis() > tupleEntry.expiryDate);
	}
	
	public long getMillisToTimeout(){
		return tupleEntry.expiryDate - System.currentTimeMillis();
	}

	//perform necessary task on database
	public abstract boolean perform();

	public void setTupleResult(String serializedTuple, String tupleClassName) {
		this.tupleResponse.setResult(serializedTuple, tupleClassName);
	}

	public Response getResponse() {
		return tupleResponse;
	}
	

}
