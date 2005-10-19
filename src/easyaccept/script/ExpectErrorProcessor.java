package easyaccept.script;

import util.ParsedLine;
import easyaccept.EasyAcceptException;

/**
 * A single object of this class is instantiated to handle all script calls to
 * the <code>expecterror</code> internal command. The <code>expecterror</code>
 * command has the following syntax:
 * <p>
 * <blockquote>
 * 
 * <pre>
 * 
 *       expecterror &quot;error message&quot; command ...
 *  
 * </pre>
 * 
 * </blockquote>
 * <p>
 * It executes the given command (usually an external command) and checks if an
 * exception was thrown. If an axception was thrown EasyAccept compares the
 * <error message> with exception.getMessage(). If those messages are identical,
 * it returns "OK"; otherwise, it throws an exception describing that the error
 * did not occur.
 * 
 * @author jacques
 */
class ExpectErrorProcessor implements Command {

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
					"Syntax error: expectError <string> <command ...>");
		}

		Result resultCommand = script.executeCommand(parsedLine.subLine(2));

		if (resultCommand.getException() != null) {
			//System.err.println("cOMMANDO:
			// "+parsedLine.getCommandString(EasyAcceptSyntax.defaultStringDelimiter));
			//System.err.println("Paramentro 1 do commando:
			// "+parsedLine.getParameter(1).getValueAsString());
			//System.err.println("Message of the exception thrown by the command:
			// "+exc.getMessage());
			//exc.printStackTrace();
			if (parsedLine.getParameter(1).getValueAsString().equals(
					resultCommand.getErrorMessage())) {
				return "OK";
			} else {
				throw new EasyAcceptException(script.getFileName(), script
						.getLineNumber(), "Expected the error message <"
						+ parsedLine.getParameter(1).getValueAsString()
						+ ">, but the error message was <"
						+ resultCommand.getErrorMessage() + ">",
						resultCommand.getException());
			}
		} else {
			throw new EasyAcceptException(script.getFileName(), script
					.getLineNumber(), "Expected <"
					+ parsedLine.getParameter(1).getValueAsString()
					+ ">, but no error occurred.");
		}

	}
}