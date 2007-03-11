package easyaccept.script.test;

import easyaccept.EasyAcceptException;

/**
 * EasyAccept test facade to provide Threads test. 
 * The TestThreadFacade have some simple methods to create an structure that provides 
 * Threads test. A TestThreadController object is known and execute the test methods whem
 * called by the scripts.
 * 
 * @author Magno Jefferson
 */
public class TestThreadFacade {
	
	/**
	 * Object used to make possible Thread tests.
	 */
	private TestThreadController controller;
	
	/**
	 * TestThreadFacade constructor.
	 */
	public TestThreadFacade(){
		controller = new TestThreadController();
	}
	
	/**
	 * Create an Object to make possible make tests with Repeat and ThreadPool commands.
	 */
	public void createTestObject(double value, String functionDescription) throws EasyAcceptException {
		controller.creatingTestObject(value,functionDescription);
	}
	/**
	 * Empty the register where the objects are in.
	 */
	public void clearTestObjectsRegister(){
		controller.deleteAllRegister();
	}
	
	/**
	 * Obtain the early object put at the register objects. 
	 * @return
	 * 			The last object from the list.
	 */
	public String getLastTestObjectValueAsString(){
		return controller.getLastObjectValueAsString();
	}
	/**
	 * Obtain the register objects size.
	 * @return
	 * 			The register objects size.
	 */
	public int getObjectRegisterSize(){
		return controller.getListSize();
	}

}
