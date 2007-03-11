package easyaccept.script;
import junit.framework.TestCase;
import util.ParsedLine;
import util.ParsedLineReader;
import easyaccept.script.test.TestThreadFacade;

/**
 * Tests the ExecuteScriptProcessor command.
 * @author Magno Jefferson
 */
public class TestExecuteScriptProcessor extends TestCase {

	/**
	 * Execute the ExecuteScriptProcessor test.
	 * @throws Exception
	 */
	public void testExecuteScriptProcessor() throws Exception {
	
		Script script = new Script("src/easyaccept/script/test/TestExecuteScript.txt",
				new TestThreadFacade());
		
		ExecuteScriptProcessor scriptExecutor;
		scriptExecutor = new ExecuteScriptProcessor();
		ParsedLineReader plr = script.getParsedLineReader();
		ParsedLine pl = plr.getParsedLine();
		Object output = scriptExecutor.execute(script,pl);
		assertTrue("true".equals(output.toString()));
	}
}
