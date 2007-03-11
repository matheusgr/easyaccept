package easyaccept.script;

import util.ParsedLine;
import util.Variables;
import util.VariablesImpl;
import easyaccept.EasyAccept;
import easyaccept.EasyAcceptException;

/**
 * Processes calls to the <code>executeScript</code> command, which is used to execute a script from within another. 
 * The new script can either be executed in a new thread or halts the current script's execution.
 * 
 * <code>executeScript</code> command has syntax:
 * <p>
 * <blockquote>
 * 
 * <pre>   
 * 
 * 
 * 			executeScript <newThread> <scriptFile>
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
public class ExecuteScriptProcessor implements Command {
	/**
	 * Submit the script execution.
	 */
	public Object execute(Script script, ParsedLine parsedLine)throws Exception {
	
		if (parsedLine.numberOfParameters() != 3) {
			throw new EasyAcceptException(script.getFileName(), script
					.getLineNumber(),
			"Syntax error: executeScript <newThread> <scriptFile>");
		}
		Object facade = script.getFacade();
		String fileName = script.getFileName();
		String testFileName = fileName.substring(0, fileName.lastIndexOf("/") + 1).concat(parsedLine.getParameter(2).getValueAsString());
		Variables variables = new VariablesImpl();		
		String newThreadInformation = parsedLine.getParameter(1).getValueAsString();
		
		if ("true".equalsIgnoreCase(newThreadInformation)){
			if (ThreadPoolProcessor.threadPool != null){
				Runnable newScript = new Script(testFileName, facade, variables);
				ThreadPoolProcessor.threadPool.execute(newScript);
			} else {
				throw new EasyAcceptException("Thread Pool was not initialized");
			}
			return "OK";
		} else {
			EasyAccept executor = new EasyAccept();
			return executor.runAcceptanceTest(facade, testFileName, variables);
		}
	}

}
	
