/*
 * Created on Oct 28, 2004
 *
 * @auth
 */
package easyaccept.script;


import util.ParsedLine;
import easyaccept.EasyAcceptException;
import easyaccept.QuitSignalException;


/**
 * Provide the Quit command.
 * @author roberta
 *
 */
public class QuitProcessor  implements Command{

	/**
	 * Execute the Quit command.
	 */
	public Object execute(Script script, ParsedLine parsedLine) throws Exception {
		
    	if (parsedLine.numberOfParameters() != 1) {
            throw new EasyAcceptException(script.getFileName(), script
                    .getLineNumber(),
                   "Syntax error: quit");
        }
    	throw new QuitSignalException(script.getFileName(), script
                .getLineNumber(), "Quit command was found.\nTerminating EasyAccept...");
	}
    	
}
