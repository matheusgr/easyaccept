/*
 * Created on 23/02/2005
 *
 */
package easyaccept.script;

import util.ParsedLine;
import util.StringUtil;
import easyaccept.EasyAcceptException;

/**
 * Provide the StackTrace command.
 * @author jacques
 * 
 */
public class StackTraceProcessor implements Command {
	/**
	 * Execute the StackTrace command.
	 */
	public Object execute(Script script, ParsedLine parsedLine)
			throws EasyAcceptException {
		if (parsedLine.numberOfParameters() < 2) {
			throw new EasyAcceptException(script.getFileName(), script
					.getLineNumber(), "Syntax error: stackTrace <command ...>");
		}
		Result resultCommand = script.executeCommand(parsedLine.subLine(1));
		if (true) {
			// produce an error so the trace can be printed
			throw new EasyAcceptException(script.getFileName(), script
					.getLineNumber(),
					resultCommand.getException() != null ? StringUtil
							.exceptionToString(resultCommand.getException())
							: " (No exception thrown.)");
		}
		return null;
	}
}