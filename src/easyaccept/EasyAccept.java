package easyaccept;

import java.io.FileNotFoundException;
import java.io.IOException;

import util.ParsingException;
import util.Variables;
import util.VariablesImpl;
import easyaccept.script.Script;

/**
 * The <code>EasyAccept</code> class is the entry point for EasyAccept.
 * The main method allows one to execute acceptance tests using scripts
 * that are executed against a faacde object giving access to the functionality
 * of the software being tested.
 * <p>
 * Here is an example of how a "topogiggio" software product would be tested.
 * <p><blockquote><pre>
 *     java -classpath ... easyaccept.EasyAccept topogiggio.TopogiggioFacade tests/script1.txt tests/script2.txt
 * </pre></blockquote><p>
 * <p>
 * This assumes that the software to be tested can be accessed through the single object
 * <code>topogiggio.TopogiggioFacade</code> and that acceptance tests are present in
 * the two files <code>script1.txt</code> and <code>scripts.txt</code>.
 * <p>
 * An exit code of 0 implies that all tests have passed.
 *  
 * @author jacques
 */
public class EasyAccept {
	
	/**
	 * Entry point for EasyAccept package.
	 * <p>
	 * Allows one to run a series of acceptance tests against a facade object 
	 * giving access to the functionality of the software to be tested.
	 *  
	 * @param args name of the facade class to be instantiated followed by script files
	 */
	public static void main(String[] args) {
		if (args.length < 2) {
			syntax();
		}
		EasyAccept tester = new EasyAccept();
		String facadeName = args[0];
		int statusCode = 0;
		try {
			Class facadeClass = Class.forName(facadeName);
			Object facade = facadeClass.newInstance();
			for (int i = 1; i < args.length; i++) {
				if(!tester.runAcceptanceTest(facade, args[i])) {
					statusCode = 1;
				}
			}
		} catch (ClassNotFoundException e1) {
			System.err.println("Facade not found: " + facadeName);
			statusCode = 1;
		}catch (QuitSignalException e1) {
			System.out.println(e1.getMessage());
			statusCode = 0;
		} catch (Exception e) {
			e.printStackTrace();
			statusCode = 1;
		}
	
		// ant doesnt like my doing an exit(0), so ...
		if(statusCode != 0) {
			System.exit(statusCode);
		}
	}

	/**
	 * Prints the syntax of the EasyAccept command.
	 * Never returns.  
	 */
	private static void syntax() {
		System.err.println("Syntax: java easyaccept.EasyAccept facadeClassName testFile [...]");
		System.exit(1);
	}

	/**
	 * Runs acceptance tests taken from a script file.
	 * The script is transformed into calls to the facade object.
	 * The results of the tests are printed on standard output.
	 *  
	 * @param facade The facade object used to exercise the functionality to be tested. 
	 * @param testFileName The file containing a script with acceptance tests. 
	 * @return true if the tests all passed; otherwise returns false.
	 * @throws IOException if problems occur when reading the script file.
	 * @throws FileNotFoundException if the script file cannot be found.
	 */
	public boolean runAcceptanceTest(Object facade, String testFileName) throws IOException, FileNotFoundException, QuitSignalException, ParsingException {
		boolean statusOK = false;
		Script script = null;
		Variables variables = new VariablesImpl();
		try {
			script = new Script(testFileName, facade, variables);
			statusOK = script.executeAndCheck();
			if (!statusOK){
				System.out.println("Test file " + testFileName + ": " + script.numberOfErrors() + " errors:");
				System.out.println(script.allErrorMessages());
			} else {
				System.out.println("Test file " + testFileName + ": " + script.numberOfTests() + " tests OK");
			}
			script.close();
		
		}catch (QuitSignalException e) {
			//System.err.println("NUM OF ERR: "+script.numberOfErrors());
			if (!script.check()){
				System.out.println("Test file " + testFileName + ": " + script.numberOfErrors() + " errors:");
				System.out.println(script.allErrorMessages());
			} else {
				System.out.println("Test file " + testFileName + ": " + script.numberOfTests() + " tests OK");
			}
			throw e;
		} catch (EasyAcceptException e) {
			statusOK = false;
			System.err.println(e.getMessage());
		} catch (EasyAcceptInternalException e) {
			statusOK = false;
			System.err.println(e.getMessage());
		}
		
		
		return statusOK;
	}

}
// TODO provide a means of choosing message formatting (como log4J)
//TODO when EA calls a method in the Facade and an exception is thrown (and EA didn't
// expect it), nothing is said about WHERE such exception occurred .. For example:
//
//Command: <rollDice firstDieResult="1"(java.lang.Byte)
//secondDieResult="1"(java.lang.Byte)>, produced error: <This place
//can't be sold>
//
// Would be nice to show the line of Java code and, if possible, the
// line of acceptance test code
// TODO pass collections of strings as parameters
//	ex. command param={ abc, "def, ghi", "{"}
//TODO javadoc
// TODO command returning collection
	//In the current implementation, a command returns a simple string. When a collection is returned, the expected string
	//should have the syntax {"s1","s2",...}
	//	where s1 is the string representing the first object in the collection, etc.
	//	EasyAccept must take care to produce a string with this syntax before testing with the expected string whenever a business logic command returns a collection.
	//	example:
	//	expect {"John Doe","Mary Stuart"} getUserNames age=20
	//	In this case, the getUserNames command returns a collection.
//TODO treatment of collection can be better by extracting an attribute from
//		each object in a returned collection
//		example:
//		expect {"John Doe","Mary Stuart"} whenExtractingAttribute attribute=name getStudents class="abc"
//	 TODO print command: 
	//	a print command that simply executes a command and prints the result returned
// TODO make test management easier
//	(test definition, packaging, execution, traceability, creation, etc. etc.)
// this user story must be expanded
// TODO command to stop on first error to avoid long list of errors. Or maybe a maxErrorToReport
