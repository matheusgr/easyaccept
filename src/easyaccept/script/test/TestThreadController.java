package easyaccept.script.test;

import java.util.ArrayList;
import java.util.List;

/**
 * TestController class implements some methods to make possible Threads tests. TestController
 * have a list to simulate objects register, and so "TestThreadObject" objects were used to do the
 * simulations because it make simple to do object and register manipulations. 
 *  
 * @author Magno Jefferson
 */
public class TestThreadController {
	
	private List<TestThreadObject> register; 
	private TestThreadObject testObject;
	
	/**
	 * TestController constructor.
	 */
	public TestThreadController(){
		register = new ArrayList<TestThreadObject>();
	}
	
	/**
	 * Criate a test object and put it at the Register
	 * @param value
	 * 			The testObject value.
	 */
	public synchronized void creatingTestObject(double value,String function) {
		testObject = new TestThreadObject(value, function);
		register.add(testObject);
	}
	
	/**
	 * Empty the register.
	 */
	public synchronized void deleteAllRegister(){
		register.clear();
	}
	/**
	 * Obtain the Register size.
	 * @return
	 * 			The register size.
	 */
	public synchronized int getListSize(){
		return register.size();
	}
	/**
	 * Obtain the early test object value by the register objects. 
	 * @return
	 * 			The last test object value from the register.
	 */
	public synchronized String getLastObjectValueAsString(){
		return register.get(register.size()-1).getObjectValueAsString();
	}
	
	/**
	 * Obtain the early test object function by the register objects. 
	 * @return
	 * 			The last test object function from the register.
	 */
	public synchronized String getTestObjectFunctionDescription(){
		return register.get(register.size()-1).getObjectFunctionDescription();
	}

}
