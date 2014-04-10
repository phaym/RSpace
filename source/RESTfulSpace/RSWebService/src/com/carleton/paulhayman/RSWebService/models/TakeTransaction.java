package com.carleton.paulhayman.RSWebService.models;

import com.carleton.paulhayman.RSWebService.comm.SpaceEntry;
import com.carleton.paulhayman.RSWebService.dao.MongoDbImpl;

public class TakeTransaction extends FindMatchTransaction {
	
	public TakeTransaction(SpaceEntry tupleEntry, int transId) {
		super(tupleEntry, transId);
	}


	@Override
	public boolean findMatch() {
		
		//will return true if it is to be placed in response queue
		boolean foundMatch = MongoDbImpl.getInstance().findTupleMatch(this, true);
		return foundMatch; 
	}




}
