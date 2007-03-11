package easyaccept.script;

import util.ParsedLine;
import easyaccept.EasyAcceptException;

/**
 * A single object of this class is instantiated to handle all script calls to
 * the <code>expectDifferent</code> internal command. The <code>expectDifferent</code> command
 * has syntax:
 * <p>
 * <blockquote>
 * 
 * <pre>
 * 
 *  
 *   
 *         expectDifferent &quot;string&quot; command ...
 *    
 *   
 *  
 * </pre>
 * 
 * </blockquote>
 * <p>
 * It executes the given command (usually an external command) and checks
 * whether the returned object, when converted to a string, does <i>not</i> yield the string
 * given a first argument. If so, it return "OK"; otherwise, it throws an
 * exception describing the error that was detected.
 * 
 * @author jacques
 */
class ExpectDifferentProcessor implements Command {

	/**
	 * Executes the <code>expectDifferent</code> internal command.
	 * 
	 * @param script
	 *            the script being executed serving as context for the
	 *            <code>expect</code> internal command.
	 * @param parsedLine
	 *            the script line being executed. This line consists of the
	 *            <code>expectDifferent</code> keyword, followed by 
	 *            <code>string</code> that is not expected and by the command that should be
	 *            executed and whose output will be compared to the 
	 *            string not expected.
	 * @return "OK" if the expected string is not returned when executing the given
	 *         command.
	 * @throws EasyAcceptException
	 *             if a syntax error is detected; or if the command executed
	 *             throws an exception; or if the object returned by the
	 *             executed command matches the expected string.
	 * @see easyaccept.script.Command#execute(easyaccept.script.Script,
	 *      easyaccept.util.ParsedLine)
	 */
	public Object execute(Script script, ParsedLine parsedLine)
			throws EasyAcceptException {
		if (parsedLine.numberOfParameters() < 3) {
			throw new EasyAcceptException(script.getFileName(), script
					.getLineNumber(),
					"Syntax error: expectDifferent <string> <command ...>");
		}
		Result resultCommand = script.executeCommand(parsedLine.subLine(2));
		if (resultCommand.getException() != null) {
			throw new EasyAcceptException(script.getFileName(), script
					.getLineNumber(), "Unexpected error: "
					+ resultCommand.getErrorMessage(), resultCommand
					.getException());
		} else if (!parsedLine.getParameter(1).getValueAsString().equals(
				resultCommand.getResultAsString())) {
			return "OK";
		} else {
			throw new EasyAcceptException(script.getFileName(), script
					.getLineNumber(), "Expected different from <"
					+ parsedLine.getParameter(1).getValueAsString()
					+ ">, but was <" + resultCommand.getResultAsString() + ">");
		}
	}

}