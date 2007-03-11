package easyaccept.script;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * A test suite to do all the script tests. 
 * @author Jacques
 */
public class AllTestsScript {
	/**
	 * The test.
	 * @return
	 * 			The test suite. 
	 */
	public static Test suite() {
		TestSuite suite = new TestSuite("Test for script");
		//$JUnit-BEGIN$
		suite.addTestSuite(TestScript.class);
		suite.addTestSuite(TestVariableSubstitution.class);
		suite.addTestSuite(TestExpectErrorProcessor.class);
		suite.addTestSuite(TestEqualFilesProcessor.class);
		suite.addTestSuite(TestQuitProcessor.class);
		suite.addTestSuite(TestStackTraceProcessor.class);
		suite.addTestSuite(TestExpectWithinProcessor.class);
		//Test classes below will print tests information on the screen
		suite.addTestSuite(TestThreadPoolProcessor.class);
		suite.addTestSuite(TestRepeatProcessor.class);
		suite.addTestSuite(TestExecuteScriptProcessor.class);
		//$JUnit-END$
		return suite;
	}
}
