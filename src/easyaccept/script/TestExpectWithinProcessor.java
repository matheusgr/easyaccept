package easyaccept.script;

import junit.framework.TestCase;
import easyaccept.script.test.TestFacade;

public class TestExpectWithinProcessor extends TestCase {

	public void testWithin1() throws Exception {
		Script script = new Script("src/easyaccept/script/test/script22.txt",
				new TestFacade());

		Result result = script.getAndExecuteCommand();
		assertNotNull(result);
		assertEquals("expectWithin .1 1.0 echo 1.0", result.getCommand());
//		assertEquals("x", result.getErrorMessage());
		assertEquals("OK", result.getResult());
		assertFalse(result.hasError());
		assertEquals("(no exception)", result.getErrorMessage());

		result = script.getAndExecuteCommand();
		assertNotNull(result);
		assertEquals("expectWithin .1 1.0 echo 1.0999", result.getCommand());
//		assertEquals("x", result.getErrorMessage());
		assertEquals("OK", result.getResult());
		assertFalse(result.hasError());
		assertEquals("(no exception)", result.getErrorMessage());

		result = script.getAndExecuteCommand();
		assertNotNull(result);
		assertEquals("expectWithin .1 1.0 echo 0.9001", result.getCommand());
//		assertEquals("x", result.getErrorMessage());
		assertEquals("OK", result.getResult());
		assertFalse(result.hasError());
		assertEquals("(no exception)", result.getErrorMessage());

		result = script.getAndExecuteCommand();
		assertNotNull(result);
		assertEquals("expectWithin .1 1.0 echo 1.10000001", result.getCommand());
		assertNull(result.getResult());
		assertTrue(result.hasError());
		assertEquals("Line 7, file src/easyaccept/script/test/script22.txt: Expected <1.0>, but was <1.10000001>", result.getErrorMessage());

		result = script.getAndExecuteCommand();
		assertNotNull(result);
		assertEquals("expectWithin .1 1.0 echo 0.899999", result.getCommand());
		assertNull(result.getResult());
		assertTrue(result.hasError());
		assertEquals("Line 8, file src/easyaccept/script/test/script22.txt: Expected <1.0>, but was <0.899999>", result.getErrorMessage());

		result = script.getAndExecuteCommand();
		assertNotNull(result);
		assertEquals("expectWithin a 1.0 echo 1.0", result.getCommand());
		assertNull(result.getResult());
		assertTrue(result.hasError());
		assertEquals("Line 11, file src/easyaccept/script/test/script22.txt: Type double expected", result.getErrorMessage());

		result = script.getAndExecuteCommand();
		assertNotNull(result);
		assertEquals("expectWithin .1 a echo 1.0", result.getCommand());
		assertNull(result.getResult());
		assertTrue(result.hasError());
		assertEquals("Line 12, file src/easyaccept/script/test/script22.txt: Type double expected", result.getErrorMessage());

		result = script.getAndExecuteCommand();
		assertNotNull(result);
		assertEquals("expectWithin .1 1.0 echo a", result.getCommand());
		assertNull(result.getResult());
		assertTrue(result.hasError());
		assertEquals("Line 13, file src/easyaccept/script/test/script22.txt: Type double expected", result.getErrorMessage());

		result = script.getAndExecuteCommand();
		assertNotNull(result);
		assertEquals("expectWithin  1.0 echo 1.0", result.getCommand());
		assertNull(result.getResult());
		assertTrue(result.hasError());
		assertEquals("Line 14, file src/easyaccept/script/test/script22.txt: Type double expected", result.getErrorMessage());

		result = script.getAndExecuteCommand();
		assertNotNull(result);
		assertEquals("expectWithin .1  echo 1.0", result.getCommand());
		assertNull(result.getResult());
		assertTrue(result.hasError());
		assertEquals("Line 15, file src/easyaccept/script/test/script22.txt: Type double expected", result.getErrorMessage());

		result = script.getAndExecuteCommand();
		assertNotNull(result);
		assertEquals("expectWithin .1 1.0 echo ", result.getCommand());
		assertNull(result.getResult());
		assertTrue(result.hasError());
		assertEquals("Line 16, file src/easyaccept/script/test/script22.txt: Type double expected", result.getErrorMessage());

		result = script.getAndExecuteCommand();
		assertNull(result);
		script.close();
	}

}
