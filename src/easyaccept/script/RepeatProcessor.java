package easyaccept.script;
import util.ParsedLine;
import easyaccept.EasyAcceptException;

/**
 * Handles calls to EasyAccept's internal command <code>repeat</code>. This command executes
 * a given script command a specific number of times.
 * 
 * 
 * <code>repeat</code> command has syntax:
 * <p>
 * <blockquote>
 * 
 * <pre>   
 * 
 * 
 * 			repeat <numberOfTimes> <anyCommand...>
 *    
 *    
 * </pre>
 * 
 * </blockquote>
 * <p>
 * 
 * @author Magno Jefferson
 * @author Gustavo Pereira
 */
public class RepeatProcessor implements Command {
	
	/**
	 * Execute the repeat command. 
	 * @param
	 * 			The script to be executed repeatedly
	 * @param
	 * 			The ParsedLine object that inform the number of times to repeat.
	 */
	public Object execute(Script script, ParsedLine parsedLine) throws Exception {
		
		
		if (parsedLine.numberOfParameters() < 3) {
	        throw new EasyAcceptException(script.getFileName(), script
	                .getLineNumber(),
	                "Syntax error: repeat <numberOfTimes> <anyCommand...>");
	    }
		int numberOfTimes = parsedLine.getParameter(1).getValueAsInt();		
		ParsedLine newParsedLine = parsedLine.subLine(2);
		
		for (int i = 1; i <= numberOfTimes; i++){
			script.executeCommand(newParsedLine);
		}		
		return "OK";
	}
}