package com.carleton.paulhayman.RSpaceClient.comm;

import java.lang.reflect.Field;
import java.util.HashMap;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class TupleObject {
	
	public String name;
	public HashMap<String, Object> fields;
	public String serializedTuple;
	public String superClass;
	
	public TupleObject() {
	}
	
	public TupleObject(Object tupleObject) throws IllegalArgumentException, IllegalAccessException {
		
		//serialize object, include null fields so all fields are represented
		Gson gson = new GsonBuilder().serializeNulls().create();
		this.serializedTuple = gson.toJson(tupleObject);
		this.name = tupleObject.getClass().getCanonicalName();
		
		//set superclass, so matches can be found if it is a subclass of template
		this.superClass = tupleObject.getClass().getSuperclass().getCanonicalName();
	
		this.fields = new HashMap<String, Object>();
		convertObjToMap(tupleObject);
	}
	
	/*serialize Object to json, store fields and values for comparison in RSpace*/
	private void convertObjToMap(Object tupleObject) throws IllegalArgumentException, IllegalAccessException {
		
		//create hashmap of objects values for each field
		Field[] classFields = tupleObject.getClass().getFields();
		for(int i = 0; i < classFields.length; i++){
			String fieldName = classFields[i].getName();
			Object fieldValue =  classFields[i].get(tupleObject);
			this.fields.put(fieldName, fieldValue);
		}
	}

}
