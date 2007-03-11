package easyaccept.script;

import junit.framework.TestCase;
import util.ParsedLine;
import util.ParsedLineReader;
import easyaccept.script.test.TestThreadFacade;
/**
 * Tests the ThreadPool command.
 * @author Magno Jefferson
 *
 */
public class TestThreadPoolProcessor extends TestCase {
	
	/**
	 * Execute the test.
	 * @throws Exception
	 */
	public void testThreads() throws Exception {
		
		Script script = new Script("src/easyaccept/script/test/scriptThreadPool.txt",
				new TestThreadFacade());
		
		ThreadPoolProcessor poolSizeDefiner;
		poolSizeDefiner = new ThreadPoolProcessor();
		
		//Define pool size
		ParsedLineReader plr = script.getParsedLineReader();
		ParsedLine pl = plr.getParsedLine();
		String output = (String) poolSizeDefiner.execute(script,pl);
		assertTrue("OK".equals(output));
		
		ExecuteScriptProcessor threadExecutor;
		threadExecutor = new ExecuteScriptProcessor();
		
		//Execute script1 making "expect" tests
		plr = script.getParsedLineReader();
		pl = plr.getParsedLine();
		output= (String) threadExecutor.execute(script,pl);
		assertTrue("OK".equals(output));
		
		//Execute script2 making "expect" tests
		plr = script.getParsedLineReader();
		pl = plr.getParsedLine();
		output= (String) threadExecutor.execute(script,pl);
		assertTrue("OK".equals(output));
		
		//Execute script3 by the thread pool
		plr = script.getParsedLineReader();
		pl = plr.getParsedLine();
		output= (String) threadExecutor.execute(script,pl);
		assertTrue("OK".equals(output));
	}
}
