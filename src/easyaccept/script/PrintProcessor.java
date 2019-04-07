package easyaccept.script;

import easyaccept.EasyAcceptException;
import util.ParsedLine;


/**
 * Provide the print command.
 * @author Héricles
 * 
 */
public class PrintProcessor implements Command {
	
	/**
	 * Execute the print command.
	 */
	public Object execute(Script script, ParsedLine parsedLine) throws Exception {

		if (parsedLine.numberOfParameters() < 3) {
			throw new EasyAcceptException(script.getFileName(), script.getLineNumber(),
					"Syntax error: expect <string> <command ...>");
		}

		Result resultCommand = script.executeCommand(parsedLine.subLine(1));

		if (resultCommand.getException() != null) {
			throw new EasyAcceptException(script.getFileName(), script.getLineNumber(),
					"Unexpected error: " + resultCommand.getErrorMessage(), resultCommand.getException());
		} else {
			System.out.println(
					"Line " + script.getLineNumber() + ", file "
					+ script.getFileName()
					+ ": " + parsedLine.getParameter(1).getValueAsString()
					+ " returns <" + resultCommand.getResultAsString() + ">");
			return "OK";
		}
	}

}