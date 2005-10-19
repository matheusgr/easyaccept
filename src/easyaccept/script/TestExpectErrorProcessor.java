/*
 * Created on Oct 25, 2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package easyaccept.script;

import junit.framework.TestCase;
import util.ParsedLine;
import util.ParsedLineReader;
import easyaccept.script.test.TestFacade;

/**
 * @author roberta
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class TestExpectErrorProcessor extends TestCase {

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
