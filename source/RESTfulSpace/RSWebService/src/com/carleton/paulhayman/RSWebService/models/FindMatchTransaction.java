package com.carleton.paulhayman.RSWebService.models;

import com.carleton.paulhayman.RSWebService.comm.SpaceEntry;
import com.carleton.paulhayman.RSWebService.dao.MongoDbImpl;


public  class FindMatchTransaction extends Transaction {
	
	boolean removeResult;
	
	public FindMatchTransaction(SpaceEntry tupleEntry, int transId, boolean removeResult) {
		super(tupleEntry, transId);
		
		this.removeResult = removeResult;
	}
	
	@Override
	public boolean perform() {
		
		/*search database for object with matching fields*/
		boolean success = MongoDbImpl.getInstance().findTupleMatch(this, removeResult);
		return success;
	}
	
}
