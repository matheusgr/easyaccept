package easyaccept.script;

import junit.framework.TestCase;
import util.ParsedLine;
import util.ParsedLineReader;
import easyaccept.script.test.TestThreadFacade;
/**
 * Tests the Repeat command.
 * @author Magno Jefferson
 *
 */
public class TestRepeatProcessor extends TestCase{
	
	/**
	 * Execute the test.
	 * @throws Exception
	 */
	public void testRepeating() throws Exception {
		Script script = new Script("src/easyaccept/script/test/scriptRepeat.txt",
				new TestThreadFacade());
		
		ThreadPoolProcessor poolSizeDefiner;
		poolSizeDefiner = new ThreadPoolProcessor();
		//Define pool size
		ParsedLineReader plr = script.getParsedLineReader();
		ParsedLine pl = plr.getParsedLine();
		String output = (String) poolSizeDefiner.execute(script,pl);
		assertTrue("OK".equals(output));
		
		//Execute the test script by threads
		RepeatProcessor repeatExecutor;
		repeatExecutor = new RepeatProcessor();
		plr = script.getParsedLineReader();
		pl = plr.getParsedLine();
		output= (String) repeatExecutor.execute(script,pl);
		assertTrue("OK".equals(output));
	}
}
