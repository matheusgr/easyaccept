package easyaccept;

import easyaccept.script.Script;
import java.io.IOException;
import util.ParsingException;
import util.Variables;
/**
 * The EasyAcceptRunner is responsible for requesting a script's execution. The
 * Script Class accomplishes the execution itself.
 * @author  Win XP
 */
public class EasyAcceptRunner {
	
	private String testFileName;
	private Object facade;
	private Variables variables;
	
	
	/**
	 * Constructor of an EasyAcceptRunner object.
	 * @param testFileName
	 * 				The file name that will be tasted. 
	 * @param facade
	 * 				facade object that give access to the functionality of the software to be tested. 
	 * @param variables
	 * 
	 */
	public EasyAcceptRunner(String testFileName, Object facade, Variables variables) {
		this.testFileName = testFileName;
		this.facade = facade;
		this.variables = variables;	
	}

	/**
	 * Entry point for EasyAccept package.
	 * <p>
	 * Allows one to run a series of acceptance tests against a facade object
	 * giving access to the functionality of the software to be tested.
	 * 
	 * @param args
	 *            name of the facade class to be instantiated followed by script
	 *            files.
	 * @return statusOK
	 * 			  boolean that represents the execution status.
	 * 			            
	 * @throws ParsingException 
	 * @throws IOException 
	 * @throws EasyAcceptInternalException 
	 * @throws EasyAcceptException 
	 */
	public synchronized boolean runScript(){
		
		Script script = null;
		boolean statusOK = false;
		try {
			script = new Script(testFileName, facade, variables);
			script.run();
			statusOK = true;
			return statusOK;
		}catch(Exception e){
			return statusOK;
		}
	}
	
}
