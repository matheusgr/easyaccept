/*
 * Created on Oct 25, 2004
 *
 */
package easyaccept.script;

import junit.framework.TestCase;
import util.ParsedLine;
import util.ParsedLineReader;
import easyaccept.script.test.TestFacade;

/**
 * Provide the expect erros test. 
 * @author roberta
 *
 */
public class TestExpectErrorProcessor extends TestCase {

	/**
	 * The set up method
	 */
	protected void setUp() throws Exception {
		super.setUp();
	}

	/**
	 * The tear down method
	 */
	protected void tearDown() throws Exception {
		super.tearDown();
	}
	/**
	 * Execute the test.
	 * @throws Exception
	 */
	public void testExecute() throws Exception{
			ExpectErrorProcessor execp;
			execp = new ExpectErrorProcessor();
			Script script = new Script("src/easyaccept/script/test/scriptExpectError.txt",
					new TestFacade());
			ParsedLineReader plr = script.getParsedLineReader();
			ParsedLine pl = plr.getParsedLine();
			String output = (String) execp.execute(script,pl);
			assertTrue("OK".equals(output));
			
			
	}

}
