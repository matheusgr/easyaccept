/*
 * Created on 03/07/2005
 *
 */
package easyaccept.script;

import util.ParsedLine;

/**
 * Provide the Echo command.
 * @author Jacques
 *
 */
public class EchoProcessor implements Command {

	/**
	 * Execute the echo command.
	 */
	public Object execute(Script script, ParsedLine parsedLine) throws Exception {
		StringBuffer sb = new StringBuffer();
		String separator = "";
		for(int i = 1; i < parsedLine.numberOfParameters(); i++) {
			sb.append(separator);
			sb.append(parsedLine.getParameter(i).getValue());
			separator = " ";
		}
		return sb.toString();
	}

}
