package easyaccept;

import java.io.FileNotFoundException;
import java.io.IOException;

import util.ParsingException;
import util.Variables;
import util.VariablesImpl;

/**
 * The <code>EasyAccept</code> class is the entry point for EasyAccept. The
 * main method allows one to execute acceptance tests using scripts that are
 * executed against a facade object giving access to the functionality of the
 * software being tested.
 * <p>
 * Here is an example of how a "topogiggio" software product would be tested.
 * <p>
 * <blockquote>
 * 
 * <pre>
 *   
 *        java -classpath ... easyaccept.EasyAccept topogiggio.TopogiggioFacade tests/script1.txt tests/script2.txt
 *    
 * </pre>
 * 
 * </blockquote>
 * <p>
 * <p>
 * This assumes that the software to be tested can be accessed through the
 * single object <code>topogiggio.TopogiggioFacade</code> and that acceptance
 * tests are present in the two files <code>script1.txt</code> and
 * <code>scripts.txt</code>.
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
	 * @param args
	 *            name of the facade class to be instantiated followed by script
	 *            files
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
			Variables variables = new VariablesImpl();
			for (int i = 1; i < args.length; i++) {
				if (!tester.runAcceptanceTest(facade, args[i], variables)) {
					statusCode = 1;
				}
			}
		} catch (ClassNotFoundException e1) {
			System.err.println("Facade not found: " + facadeName);
			statusCode = 1;
		} catch (QuitSignalException e1) {
			System.out.println(e1.getMessage());
			statusCode = 0;
		} catch (Exception e) {
			e.printStackTrace();
			statusCode = 1;
		} finally {
			exit(statusCode);
		}
	}

	/**
	 * Prints the syntax of the EasyAccept command. Never returns.
	 */
	private static void syntax() {
		System.err
				.println("Syntax: java easyaccept.EasyAccept facadeClassName testFile [...]");
		exit(1);
	}

	/**
	 * Runs acceptance tests taken from a script file. The script is transformed
	 * into calls to the facade object. The results of the tests are printed on
	 * standard output.
	 * 
	 * @param facade
	 *            The facade object used to exercise the functionality to be
	 *            tested.
	 * @param testFileName
	 *            The file containing a script with acceptance tests.
	 * @param variables
	 * @return true if the tests all passed; otherwise returns false.
	 * @throws IOException
	 *             if problems occur when reading the script file.
	 * @throws FileNotFoundException
	 *             if the script file cannot be found.
	 */
	public boolean runAcceptanceTest(Object facade, String testFileName,
			Variables variables) throws IOException, FileNotFoundException,
			QuitSignalException, ParsingException {

		EasyAcceptRunner runner = new EasyAcceptRunner(testFileName, facade,
				variables);

		return runner.runScript();
	}

	/**
	 * Exit from EasyAccept by System.exit(0) or System.exit(1) depending on the
	 * statusCode.
	 * 
	 * @param statusCode
	 *            The code that represents how EasyAccept will be finalized.
	 */
	private static void exit(int statusCode) {
		System.exit(statusCode);
	}

}
// TODO Make easyaccept.script.Result give all result info rather than forcing a
// parse of the result string
// TODO Have tests come from an generic input stream and not only files. (needs
// a more generic solution)
// TODO Have a way of examining metadata for commands. Example: for "expect",
// whether or not one can assign the result object to a variable, whether it has
// parameters, etc.
// TODO allow grouping of tests under "logical blocks". Today the only grouping
// mechanism is the file
// TODO provide a means of choosing message formatting (as in log4J)
// TODO when EA calls a method in the Facade and an exception is thrown (and EA
// didn't expect it), nothing is said about WHERE such exception occurred .. For
// example:
//
// Command: <rollDice firstDieResult="1"(java.lang.Byte)
// secondDieResult="1"(java.lang.Byte)>, produced error: <This place
// can't be sold>
//
// Would be nice to show the line of Java code and, if possible, the
// line of acceptance test code
// TODO pass collections of strings as parameters
// ex. command param={ abc, "def, ghi", "{"}
// TODO javadoc
// TODO command returning collection
// In the current implementation, a command returns a simple string. When a
// collection is returned, the expected string
// should have the syntax {"s1","s2",...}
// where s1 is the string representing the first object in the collection, etc.
// EasyAccept must take care to produce a string with this syntax before testing
// with the expected string whenever a business logic command returns a
// collection.
// example:
// expect {"John Doe","Mary Stuart"} getUserNames age=20
// In this case, the getUserNames command returns a collection.
// TODO treatment of collection can be better by extracting an attribute from
// each object in a returned collection
// example:
// expect {"John Doe","Mary Stuart"} whenExtractingAttribute attribute=name
// getStudents class="abc"
// TODO print command:
// a print command that simply executes a command and prints the result returned
// TODO make test management easier
// (test definition, packaging, execution, traceability, creation, etc. etc.)
// this user story must be expanded
// TODO command to stop on first error to avoid long list of errors. Or maybe a
// maxErrorToReport
