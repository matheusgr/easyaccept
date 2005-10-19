/*
 * Unit Test to QuitProcessor class
 * @author  
 */
package easyaccept.script;

import junit.framework.TestCase;
import util.ParsedLine;
import util.ParsedLineReader;
import easyaccept.QuitSignalException;
import easyaccept.script.test.TestFacade;

/**
 * @author roberta
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class TestQuitProcessor extends TestCase {

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
		
		QuitProcessor eqp;
		
		eqp = new QuitProcessor();
		Script script = new Script("src/easyaccept/script/test/scriptQuit01.txt",
				new TestFacade());
		ParsedLineReader plr = script.getParsedLineReader();
		ParsedLine pl = plr.getParsedLine();
		try{
			eqp.execute(script,pl);
			fail();
		}catch(QuitSignalException exc){
			assertTrue( (exc.getMessage().indexOf("Quit command was found")!= -1));
		}
	}

		
}
