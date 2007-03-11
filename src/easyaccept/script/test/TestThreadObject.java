package easyaccept.script.test;

import java.math.BigDecimal;

/**
 * A test Object that makes possible Thread tests. The Object was simple designed and provide methods
 * about a value and a function. The idea of putting this atributes refers to make simple accomplish some
 * tests.
 * 
 * @author Magno Jefferson
 */
public class TestThreadObject {
	
	private String function="";
	private BigDecimal testValue=null;
	
	/**
	 * Default TestThreadObject constructor.
	 */
	public TestThreadObject(){
		function = "provide tests";
		testValue = new BigDecimal(1000*Math.random());
	}
	
	/**
	 * TestThreadObject constructor.
	 * @param value
	 * 			That represents the test object value.
	 * @param objectFunction
	 * 			That represents the test object function description.
	 */
	public TestThreadObject(double value, String objectFunction){
		function = objectFunction;
		testValue = new BigDecimal(value);
	}
	
	/**
	 * Obtain the test Object value as string
	 * @return
	 * 			String representing the value.
	 */
	public String getObjectValueAsString(){
		return testValue.toString();
	}
	
	/**
	 * Obtain the test Object function description as string
	 * @return
	 * 			The String representing the function.
	 */
	public String getObjectFunctionDescription(){
		return function;
	}

}
