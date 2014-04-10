package com.carleton.paulhayman.RSWebService.models;

import com.carleton.paulhayman.RSWebService.comm.SpaceEntry;
import com.carleton.paulhayman.RSWebService.dao.MongoDbImpl;


public  class FindMatchTransaction extends Transaction {
	
	boolean removeResult;
	
	public FindMatchTransaction(SpaceEntry tupleEntry, int transId, boolean removeResult) {
		super(tupleEntry, transId);
		
		this.removeResult = removeResult;
	}
	
	public boolean findMatch() {
		
		//will return true if it is to be placed in response queue
		boolean foundResult = MongoDbImpl.getInstance().findTupleMatch(this, removeResult);
		return foundResult;// if readIfExists true, send whether we found result or not
	}
	
	@Override
	public boolean perform() {
		
		/*search database for object with matching fields*/
		boolean success = findMatch(); 
		return success;
	}
	
}
