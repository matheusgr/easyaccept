package easyaccept.script;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import util.ParsedLine;
import easyaccept.EasyAcceptException;

/**
 * Handles calls to EasyAccept internal command <code>threadPool</code> that 
 * creates a thread pool with a specific pool size. Threads from this pool are used by EasyAccept
 * to execute scripts concurrently.
 * 
 * <code>threadPoll</code> command has syntax:
 * <p>
 * <blockquote>
 * 
 * <pre>   
 * 
 * 
 * 			threadPool <poolsize>
 *    
 *    
 * </pre>
 * 
 * </blockquote>
 * <p>
 * 
 * @author Magno Jefferson
 * @author Gustavo Pereira
 *
 */
public class ThreadPoolProcessor implements Command {

	/**
	 * The thread pool
	 */
	public static ExecutorService threadPool;
	
	/**
	 * This method execute a Thread pool.
	 */
	public Object execute(Script script, ParsedLine parsedLine) throws Exception {

		if (parsedLine.numberOfParameters() != 2) {
            throw new EasyAcceptException(script.getFileName(), script
                    .getLineNumber(),"Syntax error: threadPool <poolsize>");
        }
		try {
			int poolSize = parsedLine.getParameter(1).getValueAsInt();		
			threadPool = Executors.newFixedThreadPool(poolSize);
		} catch (Exception e) {
			throw new EasyAcceptException(script.getFileName(), script
                    .getLineNumber(),"Error while executing the thread pool: threadPool <poolsize>");
		}
		return "OK";
	}

}
