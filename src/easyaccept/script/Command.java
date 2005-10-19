package easyaccept.script;

import util.ParsedLine;

/**
 * The interface to be implemented by any EasyAccept internal command.
 * 
 * @author Jacques
 */
interface Command {
	/**
	 * Execute a EasyAccept internal command.
	 * 
	 * @param script the script being executed and serving as context for the internal command.
	 * @param parsedLine the script command being executed (and that refers to the internal command)
	 * @return the result of the internal command. This varies for each internal command.
	 * @throws Exception this varies for each internal command.
	 */
	Object execute(Script script, ParsedLine parsedLine) throws Exception;
}
