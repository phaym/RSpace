package com.carleton.paulhayman.RSWebService.models;

import com.carleton.paulhayman.RSWebService.comm.SpaceEntry;


public abstract class FindMatchTransaction extends Transaction {
	
	
	public FindMatchTransaction(SpaceEntry tupleEntry, int transId) {
		super(tupleEntry, transId);
		
	}
	
	public abstract boolean findMatch();
	
	@Override
	public boolean perform() {
		
		/*search database for object with matching fields*/
		boolean noFurtherAction = findMatch(); 
		return noFurtherAction;
	}
	
}
