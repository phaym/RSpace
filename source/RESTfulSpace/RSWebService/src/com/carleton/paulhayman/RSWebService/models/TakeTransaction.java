package com.carleton.paulhayman.RSWebService.models;

import com.carleton.paulhayman.RSWebService.comm.SpaceEntry;
import com.carleton.paulhayman.RSWebService.dao.MongoDbImpl;

public class TakeTransaction extends FindMatchTransaction {
	
	boolean takeIfExists;
	
	public TakeTransaction(SpaceEntry tupleEntry, int transId, boolean takeIfExists) {
		super(tupleEntry, transId);
		
		this.takeIfExists = takeIfExists;
	}


	@Override
	public boolean findMatch() {
		
		//will return true if it is to be placed in response queue
		boolean foundMatch = MongoDbImpl.getInstance().findTupleMatch(this, true);
		return (foundMatch || takeIfExists); //if takeIfExists true, send whether we found result or not
	}




}
