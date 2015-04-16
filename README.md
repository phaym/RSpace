RSpace
======
RSpace is a lightweight and flexible implementation of a tuple space using RESTful web services.

Existing implementations of a tuple space such as Javaspaces have many limitations. Javaspaces can only be accessed with Java and relies on Jini making it complex, slow and difficult to deploy. This project was created to eliminate the need for Jini and the use of Java to make a simpler implementation of a tuple space that is more practical for use in a distributed system. 

Creating a tuple space that can be interacted with through the use of RESTful web services also allows for access by different programming languages and can be used on mobile devices such as Android.

Consists of server and client library written in Java using Restlet framework, Tomcat, Jersey and MongoDB for the backend.

##How To Use RSpace

The client library for this project was written in Java, although implementations are possible in other languages because of the use of RESTful web service endpoints. The following methods can be invoked in the client library to access the tuple space:  

* **write**(Object tuple, long timeout) : places a new object into the space
* **take**(Object template, long timeout) : retrieves object from a space
* **read**(Object template, long timeout) : makes a copy of an object in the space  

Both **take** and **read** are blocking operations that will return a match when it is found in the space before a provided timeout length, or return null after the timeout has expired and no match is found. They each have variants:  
* **takeIfExists**(Object template)  
* **readIfExists**(Object template)

These are non-blocking operations and do not require a timeout length as they immediately return a result whether a match is found or not. In other words, they are read and take operations with a timeout set to 0. If a match is not found at the time these operations are called, theses methods will return null.  

##Using Template to Find a Tuple

The template provided as input for responsive transactions will be used to search for a desired tuple. If a match is not found before the provided timeout, the result of these calls will be null. A match will be found based on the following conditions:
* If a field in the template object is null, this is considered a wild card, and the tuple returned could have any value for that field.
* If a field in the template object is set to a particular value then the tuple returned will have an equal value in the same field.
* A successful match will return a tuple that is of the same class or a subclass of the template.
* A successful match will return a tuple that has not yet expired.
	
Templates, therefore, can be created with no fields specified, all fields specified or some fields specified in order to retrieve a desired tuple.  

##Examples

The following examples use the following TestObject class: 
```
public class TestObject  {
	public Integer intField;
	public String stringField;
	public Boolean boolField;
	
	TestObject(){}
	
	TestObject(Integer num, String string, Boolean bool){
		intField = num;
		stringField = string;
		boolField = bool;
	}
}
```
###Example 1 – using a template with no fields initialized
```
public static void main(String[] args){
		RSpace space = RSpace.getSpace();
		
		/*write tuple to space*/
		TestObject tupleToWrite = new TestObject(99, "HelloWorld", true);
		space.write(tupleToWrite, 30000);
		
		/*create uninitialized template*/
		TestObject template = new TestObject();
		TestObject result = (TestObject) space.read(template, 5000);
		
		/*show result as serialized JSON**/
		Gson gson = new GsonBuilder().serializeNulls().create();
		System.out.println(gson.toJson(result));
	}
```	

**Output :** {"intField":99,"stringField":"HelloWorld","boolField":true}

As is demonstrated in the output, when calling read using a template where all fields are null, a tuple is returned with fields that could be set to any value. In this case it returned the “tupleToWrite” object, as this was the only tuple in the space that was a match.

###Example 2 – using a template with fields specified
```
public static void main(String[] args){
	RSpace space = RSpace.getSpace();
	
	/*write tuples to space*/
	TestObject tupleToWrite1 = new TestObject(99, "HelloWorld", true);
	TestObject tupleToWrite2 = new TestObject(33, "A String", false);
	space.write(tupleToWrite1, 30000);
	space.write(tupleToWrite2, 30000);
	
	/*create uninitialized template*/
	TestObject template = new TestObject(99, null, null);
	TestObject result = (TestObject) space.read(template, 5000);
	
	/*show result as serialized JSON**/
	Gson gson = new GsonBuilder().serializeNulls().create();
	System.out.println(gson.toJson(result));
}
```
**Output:** {"intField":99,"stringField":"HelloWorld","boolField":true}

In this example, two tuples of the same class were written to the space with different values in their fields. A template object was created to search for any TestObject with “intField” set to 99 and used to read from the space. This returned a tuple which is a copy of the object “tupleToWrite1”, the tuple in the space that contained an equal value in the “intField”.
