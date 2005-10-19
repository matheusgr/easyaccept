/*
 * Created on 10/06/2004
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package util;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * @author Jacques
 *
 * To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public class AllTestsUtil {

	public static Test suite() {
		TestSuite suite = new TestSuite("Test for util");
		//$JUnit-BEGIN$
		suite.addTestSuite(TestMultiFileReader.class);
		suite.addTestSuite(TestLogicalLineReader.class);
		suite.addTestSuite(TestParsedLineReader.class);
		suite.addTestSuite(TestParameterTypeConverter.class);
		//$JUnit-END$
		return suite;
	}
}
