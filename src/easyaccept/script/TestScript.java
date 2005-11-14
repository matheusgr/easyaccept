package easyaccept.script;

import java.io.IOException;

import junit.framework.TestCase;
import util.ParsingException;
import util.Variables;
import util.VariablesImpl;
import easyaccept.EasyAcceptException;
import easyaccept.EasyAcceptInternalException;
import easyaccept.QuitSignalException;
import easyaccept.script.test.TestFacade;

public class TestScript extends TestCase {
	public void testScript1() throws Exception {
		Script script = new Script("src/easyaccept/script/test/script1.txt",
				new TestFacade());
		Result result = script.getAndExecuteCommand();
		assertNull(result);
		script.close();
	}

	public void testScript2() throws Exception {
		Script script = new Script("src/easyaccept/script/test/script2.txt",
				new TestFacade());
		Result result = script.getAndExecuteCommand();
		assertNotNull(result);
		assertEquals("commandReturningString", result.getCommand());
		assertFalse(result.hasError());
		assertEquals("(no exception)", result.getErrorMessage());
		assertEquals("result commandReturningString", result.getResult());
		result = script.getAndExecuteCommand();
		assertNull(result);
		script.close();
	}

	public void testScript3() throws Exception {
		Script script = new Script("src/easyaccept/script/test/script3.txt",
				new TestFacade());

		Result result = script.getAndExecuteCommand();
		assertNotNull(result);
		assertTrue(result.getCommand().indexOf("stringdelimiter") != -1);
		assertFalse(result.hasError());
		assertEquals("(no exception)", result.getErrorMessage());
		assertEquals("OK", result.getResult());

		result = script.getAndExecuteCommand();
		assertNotNull(result);
		assertEquals("commandReturningString param1=hello", result.getCommand());
		assertFalse(result.hasError());
		assertEquals("(no exception)", result.getErrorMessage());
		assertEquals("HELLO", result.getResult());

		result = script.getAndExecuteCommand();
		assertNotNull(result);
		assertEquals("commandReturningString param1=\" hello \"", result
				.getCommand());
		assertFalse(result.hasError());
		assertEquals("(no exception)", result.getErrorMessage());
		assertEquals(" HELLO ", result.getResult());

		result = script.getAndExecuteCommand();
		assertNull(result);
		script.close();
	}

	public void testScript4() throws Exception {
		Script script = new Script("src/easyaccept/script/test/script4.txt",
				new TestFacade());

		Result result = script.getAndExecuteCommand();
		assertNotNull(result);
		assertEquals("throwException message=\"An Exception\"", result
				.getCommand());
		assertNull(result.getResult());
		assertTrue(result.hasError());
		assertEquals("An Exception", result.getErrorMessage());

		result = script.getAndExecuteCommand();
		assertNotNull(result);
		assertEquals("qwerty", result.getCommand());
		assertNull(result.getResult());
		assertTrue(result.hasError());
		assertEquals("Unknown command: qwerty", result.getErrorMessage());

		result = script.getAndExecuteCommand();
		assertNull(result);
		script.close();
	}

	public void testScript5() throws Exception {
		Script script = new Script("src/easyaccept/script/test/script5.txt",
				new TestFacade());

		Result result = script.getAndExecuteCommand();
		assertNotNull(result);
		assertEquals("expect HELLO commandReturningString param1=hello", result
				.getCommand());
		assertEquals("OK", result.getResult());
		assertFalse(result.hasError());
		assertEquals("(no exception)", result.getErrorMessage());

		result = script.getAndExecuteCommand();
		assertNotNull(result);
		assertEquals(
				"expect \"HELLO WORLD\" commandReturningString param1=\"hello world\"",
				result.getCommand());
		assertEquals("OK", result.getResult());
		assertFalse(result.hasError());
		assertEquals("(no exception)", result.getErrorMessage());

		result = script.getAndExecuteCommand();
		assertNull(result);
		script.close();
	}

	public void testScript6() throws Exception {
		Script script = new Script("src/easyaccept/script/test/script6.txt",
				new TestFacade());

		Result result = script.getAndExecuteCommand();
		assertNotNull(result);
		assertEquals("expect", result.getCommand());
		assertNull(result.getResult());
		assertTrue(result.hasError());
		assertEquals(
				"Line 1, file src/easyaccept/script/test/script6.txt: Syntax error: expect <string> <command ...>",
				result.getErrorMessage());

		result = script.getAndExecuteCommand();
		assertNotNull(result);
		assertEquals("expect HELLO", result.getCommand());
		assertNull(result.getResult());
		assertTrue(result.hasError());
		assertEquals(
				"Line 2, file src/easyaccept/script/test/script6.txt: Syntax error: expect <string> <command ...>",
				result.getErrorMessage());

		result = script.getAndExecuteCommand();
		assertNull(result);
		script.close();
	}

	public void testScript7() throws Exception {
		Script script = new Script("src/easyaccept/script/test/script5.txt",
				new TestFacade());
		assertTrue(script.executeAndCheck());
		assertEquals(2, script.getResults().size());
		script.close();
	}

	public void testScript8() throws Exception {
		Script script = new Script("src/easyaccept/script/test/script6.txt",
				new TestFacade());
		assertFalse(script.executeAndCheck());
		assertEquals(2, script.getResults().size());
		assertEquals(
				"Line 1, file src/easyaccept/script/test/script6.txt: Syntax error: expect <string> <command ...>"
						+ System.getProperty("line.separator")
						+ "Command producing error: <expect>"
						+ System.getProperty("line.separator")
						+ "Line 2, file src/easyaccept/script/test/script6.txt: Syntax error: expect <string> <command ...>"
						+ System.getProperty("line.separator")
						+ "Command producing error: <expect HELLO>"
						+ System.getProperty("line.separator"), script
						.allErrorMessages());
		script.close();
	}

	public void testScript9() throws Exception {
		Script script = new Script("src/easyaccept/script/test/script7.txt",
				new TestFacade());

		Result result = script.getAndExecuteCommand();
		assertNotNull(result);
		assertTrue(result.getCommand().indexOf("stringdelimiter") != -1);
		assertEquals("OK", result.getResult());
		assertFalse(result.hasError());
		assertEquals("(no exception)", result.getErrorMessage());

		result = script.getAndExecuteCommand();
		assertNotNull(result);
		assertEquals("expect HELLO commandReturningString param1=hello", result
				.getCommand());
		assertEquals("OK", result.getResult());
		assertFalse(result.hasError());
		assertEquals("(no exception)", result.getErrorMessage());

		result = script.getAndExecuteCommand();
		assertNotNull(result);
		assertEquals(
				"expect 'HELLO WORLD' commandReturningString param1='hello world'",
				result.getCommand());
		assertEquals("OK", result.getResult());
		assertFalse(result.hasError());
		assertEquals("(no exception)", result.getErrorMessage());

		result = script.getAndExecuteCommand();
		assertNull(result);
		script.close();
	}

	public void testScript10() throws Exception {
		Script script = new Script("src/easyaccept/script/test/script8.txt",
				new TestFacade());
		Result result = script.getAndExecuteCommand();
		assertNotNull(result);
		assertEquals("expect OK throwException message=\"exception message\"",
				result.getCommand());
		assertNull(result.getResult());
		assertTrue(result.hasError());
		assertEquals(
				"Line 1, file src/easyaccept/script/test/script8.txt: Unexpected error: exception message",
				result.getErrorMessage());

		result = script.getAndExecuteCommand();
		assertNull(result);
		script.close();
	}

	public void testScript11() throws Exception {
		Script script = new Script("src/easyaccept/script/test/script9.txt",
				new TestFacade());
		Result result = script.getAndExecuteCommand();
		assertNotNull(result);
		assertEquals("expect HELLOO commandReturningString param1=hello", result
				.getCommand());
		assertNull(result.getResult());
		assertTrue(result.hasError());
		assertEquals(
				"Line 1, file src/easyaccept/script/test/script9.txt: Expected <HELLOO>, but was <HELLO>",
				result.getErrorMessage());

		result = script.getAndExecuteCommand();
		assertNull(result);
		script.close();
	}

	public void testScript12() throws Exception {
		Script script = new Script("src/easyaccept/script/test/script10.txt",
				new TestFacade());
		Result result = script.getAndExecuteCommand();
		assertNotNull(result);
		assertEquals("expect true returnTrue", result.getCommand());
		assertFalse(result.hasError());
		assertEquals("(no exception)", result.getErrorMessage());
		assertEquals("OK", result.getResult());

		result = script.getAndExecuteCommand();
		assertNull(result);
		script.close();
	}

	public void testMethodMatch() throws Exception {
		Script script = new Script("src/easyaccept/script/test/script11.txt",
				new TestFacade());
		Result result = script.getAndExecuteCommand();
		assertNotNull(result);
		assertEquals("commandManyParameters param1=hello param2=hellow", result
				.getCommand());
		//System.err.println(result.getErrorMessage());
		//result.getException().printStackTrace();
		assertFalse(result.hasError());

		result = script.getAndExecuteCommand();
		assertNotNull(result);
		assertEquals("commandManyParametersNum param1=123 param2=456", result
				.getCommand());
		//System.err.println(result.getErrorMessage());
		//result.getException().printStackTrace();
		assertFalse(result.hasError());

		result = script.getAndExecuteCommand();
		assertNotNull(result);
		assertEquals(
				"expect 123456 commandManyParametersNum param1=123 param2=456",
				result.getCommand());
		assertFalse(result.hasError());

		result = script.getAndExecuteCommand();
		assertNotNull(result);
		assertEquals(
				"expect 5 commandManyParametersPrimitive param1=2 param2=3",
				result.getCommand());
		assertFalse(result.hasError());

		script.close();
	}

	public void testVariableSubstitution1() throws Exception {
		Script script = new Script("src/easyaccept/script/test/script13.txt",
				new TestFacade());
		Result result = script.getAndExecuteCommand();
		assertNotNull(result);
		assertEquals("a=commandReturningString param1=hello", result.getCommand());
		assertFalse(result.hasError());
		assertEquals("HELLO", script.getVariableValue("a"));

		result = script.getAndExecuteCommand();
		assertNotNull(result);
		assertEquals("expect HELLO commandReturningString param1=hello", result
				.getCommand());
		assertFalse(result.hasError());

	}

	public void testVariableSubstitution2() throws Exception {
		Script script = new Script("src/easyaccept/script/test/script14.txt",
				new TestFacade());

		Result result = script.getAndExecuteCommand();
		assertNotNull(result);
		assertEquals("expect HELLO b=commandReturningString param1=hello", result
				.getCommand());
		assertFalse(result.hasError());

		result = script.getAndExecuteCommand();
		assertNotNull(result);
		assertEquals("expect HELLO commandReturningString param1=hello", result
				.getCommand());
		assertFalse(result.hasError());

	}

	public void testVariableSubstitution3() throws Exception {
		Script script = new Script("src/easyaccept/script/test/script15.txt",
				new TestFacade());

		Result result = script.getAndExecuteCommand();
		assertNotNull(result);
		assertEquals("d=expect HELLO c=commandReturningString param1=hello", result
				.getCommand());
		assertFalse(result.hasError());

		result = script.getAndExecuteCommand();
		assertNotNull(result);
		assertEquals("expect HELLO commandReturningString param1=hello", result
				.getCommand());
		assertFalse(result.hasError());

		result = script.getAndExecuteCommand();
		assertNotNull(result);
		assertEquals("expect \"Result is OK\" echo Result is OK",
				result.getCommand());
		assertFalse(result.hasError());
	}

	public void testVariableSubstitution4() throws EasyAcceptException,
			EasyAcceptInternalException, QuitSignalException, IOException,
			ParsingException {
		Script script = new Script("src/easyaccept/script/test/script16.txt",
				new TestFacade());

		Result result = script.getAndExecuteCommand();
		assertNotNull(result);
		assertEquals("a=commandReturningInt param1=46", result.getCommand());
		assertFalse(result.hasError());

		result = script.getAndExecuteCommand();
		assertNotNull(result);
		assertEquals("expect 47 echo 47", result.getCommand());
		assertFalse(result.hasError());
	}

	public void testVariableSubstitution5() throws EasyAcceptException, EasyAcceptInternalException, QuitSignalException, IOException, ParsingException {
		Script script = new Script("src/easyaccept/script/test/script17.txt",
				new TestFacade());

		Result result = script.getAndExecuteCommand();
		assertNotNull(result);
		assertEquals("expect a=b command1", result.getCommand());
//		assertEquals("", result.getErrorMessage());
		assertFalse(result.hasError());
	}
	public void testParameterPassingSyntax() throws EasyAcceptException,
			EasyAcceptInternalException, QuitSignalException, IOException, ParsingException {
		Script script = new Script("src/easyaccept/script/test/script18.txt",
				new TestFacade());
		for(int i = 0; i < 4; i++) {
			Result result = script.getAndExecuteCommand();
			assertNotNull(result);
//			assertEquals("", result.getErrorMessage());
			assertFalse(""+i, result.hasError());
			assertEquals("OK", result.getResult());
		}
	}
	public void testVariableSubstitutionInTwoScripts() throws EasyAcceptException, EasyAcceptInternalException, QuitSignalException, IOException, ParsingException {
		Variables variables = new VariablesImpl();
		Script script1 = new Script("src/easyaccept/script/test/script19.txt",
				new TestFacade(), variables);
		Result result = script1.getAndExecuteCommand();
		assertNotNull(result);
		assertEquals("a=commandReturningString param1=hello", result.getCommand());
		assertFalse(result.hasError());
		assertEquals("HELLO", script1.getVariableValue("a"));

		Script script2 = new Script("src/easyaccept/script/test/script20.txt",
				new TestFacade(), variables);
		result = script2.getAndExecuteCommand();
		assertNotNull(result);
		assertEquals("expect HELLO commandReturningString param1=hello", result
				.getCommand());
		assertFalse(result.hasError());
	}
}