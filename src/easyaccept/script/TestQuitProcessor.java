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
 * Provide the Quit test.
 * @author roberta
 *
 */
public class TestQuitProcessor extends TestCase {

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
	 * Execute the Quit test.
	 * @throws Exception
	 */
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
