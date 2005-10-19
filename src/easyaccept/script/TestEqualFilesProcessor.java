/*
 * Created on Oct 28, 2004
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
public class TestEqualFilesProcessor extends TestCase {

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

	public void testExecute() throws Exception {
		
		EqualFilesProcessor eqp;
		
		eqp = new EqualFilesProcessor();
		Script script = new Script("src/easyaccept/script/test/scriptEqualFiles01.txt",
				new TestFacade());
		ParsedLineReader plr = script.getParsedLineReader();
		ParsedLine pl = plr.getParsedLine();
		String output = (String) eqp.execute(script,pl);
		assertTrue("OK".equals(output));
				
		plr = script.getParsedLineReader();
		pl = plr.getParsedLine();
		try{
			output= (String) eqp.execute(script,pl);
			fail();
		}catch(Exception exc){
			//System.err.println( "Message:: " + exc.getClass().getName() + " - "+ exc.getMessage());
			assertTrue(exc.getMessage().indexOf("Expected") != -1);
			assertTrue(exc.getMessage().indexOf("but was") != -1);
		}
	}

		
}
