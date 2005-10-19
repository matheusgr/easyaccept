/*
 * Created on 23/02/2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package easyaccept.script;

import java.io.IOException;

import junit.framework.TestCase;
import util.ParsingException;
import easyaccept.EasyAcceptException;
import easyaccept.EasyAcceptInternalException;
import easyaccept.script.test.TestFacade;

/**
 * @author jacques
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public class TestStackTraceProcessor extends TestCase {

	/*
	 * @see TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		super.setUp();
	}

	/*
	 * @see TestCase#tearDown()
	 */
	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public void testUnexpectedException() throws EasyAcceptInternalException,
			EasyAcceptException, IOException, ParsingException {
		Script script = new Script(
				"src/easyaccept/script/test/script12.txt",
				new TestFacade());

		Result result = script.getAndExecuteCommand();
		assertNotNull(result);
		assertEquals(
				"throwAnUnexpectedException throwIt=false",
				result.getCommand());
		assertEquals("return string", result.getResult());
		assertFalse(result.hasError());
		assertEquals("(no exception)", result.getErrorMessage());

		result = script.getAndExecuteCommand();
		assertNotNull(result);
		assertEquals(
				"throwAnUnexpectedException throwIt=true",
				result.getCommand());
		assertNull(result.getResult());
		assertTrue(result.hasError());
		assertEquals("(no message: exception = java.lang.Exception)", result.getErrorMessage());

		result = script.getAndExecuteCommand();
		assertNotNull(result);
		assertEquals(
				"stackTrace throwAnUnexpectedException throwIt=false",
				result.getCommand());
		assertNull(result.getResult());
		assertTrue(result.hasError());
		assertTrue(contains("No exception thrown.)", result.getErrorMessage()));

		result = script.getAndExecuteCommand();
		assertNotNull(result);
		assertEquals(
				"stackTrace throwAnUnexpectedException throwIt=true",
				result.getCommand());
		assertNull(result.getResult());
		assertTrue(result.hasError());
		assertTrue(contains(
				"at easyaccept.script.test.TestFacade.throwAnUnexpectedException(TestFacade.java:",
				result.getErrorMessage()));

		result = script.getAndExecuteCommand();
		assertNotNull(result);
		assertEquals(
				"stackTrace expect \"return string\" throwAnUnexpectedException throwIt=false",
				result.getCommand());
		assertNull(result.getResult());
		assertTrue(result.hasError());
		assertTrue(contains("No exception thrown.)", result.getErrorMessage()));

		result = script.getAndExecuteCommand();
		assertNotNull(result);
		assertEquals(
				"stackTrace expect \"return string\" throwAnUnexpectedException throwIt=true",
				result.getCommand());
		assertNull(result.getResult());
		assertTrue(result.hasError());
		assertTrue(contains(
				"at easyaccept.script.test.TestFacade.throwAnUnexpectedException(TestFacade.java:",
				result.getErrorMessage()));

		result = script.getAndExecuteCommand();
		assertNotNull(result);
		assertEquals(
				"stackTrace expectError  throwAnUnexpectedException throwIt=false",
				result.getCommand());
		assertNull(result.getResult());
		assertTrue(result.hasError());
		assertTrue(contains("but no error occurred", result.getErrorMessage()));

		result = script.getAndExecuteCommand();
		assertNotNull(result);
		assertEquals(
				"stackTrace expectError null throwAnUnexpectedException throwIt=true",
				result.getCommand());
		assertNull(result.getResult());
		assertTrue(result.hasError());
		assertTrue(contains(
				"at easyaccept.script.test.TestFacade.throwAnUnexpectedException(TestFacade.java:",
				result.getErrorMessage()));

		result = script.getAndExecuteCommand();
		assertNull(result);
		script.close();

	}

	/**
	 * @param string
	 * @param errorMessage
	 * @return
	 */
	private boolean contains(String expectedString, String actualString) {
		return actualString.indexOf(expectedString) >= 0;
	}

}