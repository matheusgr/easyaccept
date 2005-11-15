package easyaccept.script;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * 
 * @author Jacques
 */
public class AllTestsScript {

	public static Test suite() {
		TestSuite suite = new TestSuite("Test for script");
		//$JUnit-BEGIN$
		suite.addTestSuite(TestScript.class);
		suite.addTestSuite(TestVariableSubstitution.class);
		suite.addTestSuite(TestExpectErrorProcessor.class);
		suite.addTestSuite(TestEqualFilesProcessor.class);
		suite.addTestSuite(TestQuitProcessor.class);
		suite.addTestSuite(TestStackTraceProcessor.class);
		//$JUnit-END$
		return suite;
	}
}
