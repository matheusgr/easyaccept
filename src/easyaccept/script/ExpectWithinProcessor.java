package easyaccept.script;

import util.ParsedLine;
import easyaccept.EasyAcceptException;

/**
 * A single object of this class is instantiated to handle all script calls to
 * the <code>expectWithin</code> internal command. The
 * <code>expectWithin</code> command has syntax:
 * <p>
 * <blockquote>
 * 
 * <pre>
 *  
 *   
 *    
 *          expectWithin &quot;double&quot; &quot;double&quot; command ...
 *     
 *    
 *   
 * </pre>
 * 
 * </blockquote>
 * <p>
 * It executes the given command (usually an external command) and checks
 * whether the returned object, when converted to a double, yields the double
 * given the second argument, with a precision given by the first argument. If
 * so, it return "OK"; otherwise, it throws an exception describing the error
 * that was detected.
 * 
 * @author jacques
 */
class ExpectWithinProcessor implements Command {

	/**
	 * Executes the <code>expectWithin</code> internal command.
	 * 
	 * @param script
	 *            the script being executed serving as context for the
	 *            <code>expectWithin</code> internal command.
	 * @param parsedLine
	 *            the script line being executed. This line consists of the
	 *            <code>expectWithin</code> keyword, followed by two expected
	 *            <code>double</code> and by the command that should be
	 *            executed and whose output will be compared to the expected
	 *            result.
	 * @return "OK" if the expected value is returned when executing the given
	 *         command, within the precision given.
	 * @throws EasyAcceptException
	 *             if a syntax error is detected; or if the command executed
	 *             throws an exception; or if the object returned by the
	 *             executed command does not match the expected result.
	 * @see easyaccept.script.Command#execute(easyaccept.script.Script,
	 *      easyaccept.util.ParsedLine)
	 */
	public Object execute(Script script, ParsedLine parsedLine)
			throws EasyAcceptException {
		if (parsedLine.numberOfParameters() < 4) {
			throw new EasyAcceptException(script.getFileName(), script
					.getLineNumber(),
					"Syntax error: expectWithin <precision> <value> <command ...>");
		}
		// System.err.println("executing <"
		// + parsedLine.subLine(3).getCommandString() + ">");
		Result resultCommand = script.executeCommand(parsedLine.subLine(3));
		if (resultCommand.getException() != null) {
			throw new EasyAcceptException(script.getFileName(), script
					.getLineNumber(), "Unexpected error: "
					+ resultCommand.getErrorMessage(), resultCommand
					.getException());
		} else {
			try {
				double within = parsedLine.getParameter(1).getValueAsDouble();
				double expected = parsedLine.getParameter(2).getValueAsDouble();
				double valueReturned = Double.parseDouble(resultCommand.getResultAsString());
//				System.err.println("within " + within);
//				System.err.println("expected " + expected);
//				System.err.println("returned " + resultCommand.getResultAsString());
				if (Math.abs(expected-valueReturned) <= within) {
					return "OK";
				} else {
					throw new EasyAcceptException(script.getFileName(), script
							.getLineNumber(), "Expected <"
							+ parsedLine.getParameter(2).getValueAsString()
							+ ">, but was <" + resultCommand.getResultAsString()
							+ ">");
				}
			} catch(NumberFormatException e) {
				throw new EasyAcceptException(script.getFileName(), script
						.getLineNumber(), "Type double expected");
			}
		}
	}

}