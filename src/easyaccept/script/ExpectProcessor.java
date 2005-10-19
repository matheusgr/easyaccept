package easyaccept.script;

import util.ParsedLine;
import easyaccept.EasyAcceptException;

/**
 * A single object of this class is instantiated to handle all script calls to
 * the <code>expect</code> internal command. The <code>expect</code> command
 * has syntax:
 * <p>
 * <blockquote>
 * 
 * <pre>
 * 
 *  
 *        expect &quot;string&quot; command ...
 *   
 *  
 * </pre>
 * 
 * </blockquote>
 * <p>
 * It executes the given command (usually an external command) and checks
 * whether the returned object, when converted to a string, yields the string
 * given a first argument. If so, it return "OK"; otherwise, it throws an
 * exception describing the error that was detected.
 * 
 * @author jacques
 */
class ExpectProcessor implements Command {

	/**
	 * Executes the <code>expect</code> internal command.
	 * 
	 * @param script
	 *            the script being executed serving as context for the
	 *            <code>expect</code> internal command.
	 * @param parsedLine
	 *            the script line being executed. This line consists of the
	 *            <code>expect</code> keyword, followed by an expected
	 *            <code>string</code> and by the command that should be
	 *            executed and whose output will be compared to the expected
	 *            string.
	 * @return "OK" if the expected string is returned when executing the given
	 *         command.
	 * @throws EasyAcceptException
	 *             if a syntax error is detected; or if the command executed
	 *             throws an exception; or if the object returned by the
	 *             executed command does not match the expected string.
	 * @see easyaccept.script.Command#execute(easyaccept.script.Script,
	 *      easyaccept.util.ParsedLine)
	 */
	public Object execute(Script script, ParsedLine parsedLine)
			throws EasyAcceptException {
		if (parsedLine.numberOfParameters() < 3) {
			throw new EasyAcceptException(script.getFileName(), script
					.getLineNumber(),
					"Syntax error: expect <string> <command ...>");
		}
		//		System.err.println("executing <"
		//				+ parsedLine.subLine(2).getCommandString() + ">");
		Result resultCommand = script.executeCommand(parsedLine.subLine(2));
		//		System.err.println("gave " + resultCommand.getResult());
		//		System.err.println("check " + parsedLine.getParameter(1).getName());
		if (resultCommand.getException() != null) {
			throw new EasyAcceptException(script.getFileName(), script
					.getLineNumber(), "Unexpected error: " + resultCommand.getErrorMessage(),
					resultCommand.getException());
		} else if (parsedLine.getParameter(1).getValueAsString().equals(
				resultCommand.getResultAsString())) {
			return "OK";
		} else {
			throw new EasyAcceptException(script.getFileName(), script
					.getLineNumber(), "Expected <"
					+ parsedLine.getParameter(1).getValueAsString()
					+ ">, but was <" + resultCommand.getResultAsString() + ">");
		}
	}

}