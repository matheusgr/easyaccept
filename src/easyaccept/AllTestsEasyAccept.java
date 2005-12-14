package easyaccept;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * 
 * @author Jacques
 *
 */
public class AllTestsEasyAccept {

	public static Test suite() {
		TestSuite suite = new TestSuite("Test for default package");
		//$JUnit-BEGIN$
		suite.addTest(util.AllTestsUtil.suite());
		suite.addTest(easyaccept.script.AllTestsScript.suite());
		//$JUnit-END$
		return suite;
	}
	
}
