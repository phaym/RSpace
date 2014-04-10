package com.carleton.paulhayman.RSWebService.models;

import com.carleton.paulhayman.RSWebService.comm.SpaceEntry;
import com.carleton.paulhayman.RSWebService.dao.MongoDbImpl;

public class ReadTransaction extends FindMatchTransaction {
	
	public ReadTransaction(SpaceEntry tupleEntry, int transId) {
		super(tupleEntry, transId);
		
	}

	@Override
	public boolean findMatch() {
		
		//will return true if it is to be placed in response queue
		boolean foundResult = MongoDbImpl.getInstance().findTupleMatch(this, false);
		return foundResult;// if readIfExists true, send whether we found result or not
	}




}
