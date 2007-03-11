/*
 * Project: topogiggio
 * Script.java 
 *
 * Copyright 2004 Universidade federal da Campina Grande. All rights reserved.
 */

package easyaccept.script;

import easyaccept.EasyAcceptException;
import easyaccept.EasyAcceptInternalException;
import easyaccept.EasyAcceptSyntax;
import easyaccept.QuitSignalException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import util.ConverterException;
import util.LogicalLineReader;
import util.MultiFileEvent;
import util.MultiFileReader;
import util.MultiFileReaderObserver;
import util.ParameterTypeConverter;
import util.ParsedLine;
import util.ParsedLineReader;
import util.ParsingException;
import util.Variables;
import util.VariablesImpl;

/**
 * The <code>Script</code> class must be instantiated for each test script to be executed. 
 * A test script is a sequence of commands present in a test file. 
 * A script may consist of two types of commands: internal and external. 
 * The internal commands are part of EasyAccept itself. 
 * So far, these commands include: <p> <blockquote> <pre> expect &quot;string&quot; command ... stringdelimiter delimiter </pre> </blockquote> <p> 
 * Each of these internal commands is explained in the corresponding "xxxProcessor" class. 
 * <p> The external commands are those available through the facade providing access to the functionality of 
 * the software being tested. Basically, any public method available in the facade can be called in the script. 
 * The return value can then be tested (using an internal command such as <code>expect</code>).
 *  <p> Once a script is created, it can be executed, either one command at a time or all commands at once. 
 *  Results can be obtained about the execution of a script's commands.
 * @author  jacques
 */
public class Script implements MultiFileReaderObserver, Runnable {

	/**
	 * Table to check if a command is an internal command.
	 */
	private Map<String, Command> internalCommands;
	/**
	 * Provides access to the script file, with automatic parsing of the command
	 * line.
	 */
	private ParsedLineReader plr;
	/**
	 * The facade object giving access to the software being tested.
	 */
	private Object facade;
	/**
	 * A collection of results, one for each command in the script.
	 */
	private Collection<Result> results;
	/**
	 * The stringDelimiter is needed here so that we can format the command with
	 * the same syntax as the script file when reporting errors.
	 */
	private char stringDelimiter;
	

	
	/**
	 * Construct a Script object. This should be done for each script file to be
	 * executed.
	 * 
	 * @param fileName
	 *            The name of the file containing the test script.
	 * @param facade
	 *            The facade object giving access to the functionality of the
	 *            software to be tested
	 * @param variables
	 *            The Map to hold script variables during execution
	 * @throws FileNotFoundException
	 * @throws EasyAcceptException
	 *             if the facade object was not given.
	 * @throws FileNotFoundException
	 *             if the script file cannot be found.
	 * @throws EasyAcceptException
	 * @throws EasyAcceptInternalException
	 */
	public Script(String fileName, Object facade, Variables variables)
			throws FileNotFoundException, EasyAcceptException,
			EasyAcceptInternalException {
		MultiFileReader mfReader = new MultiFileReader();

		plr = new ParsedLineReader(new LogicalLineReader(mfReader,
				EasyAcceptSyntax.defaultComment,
				EasyAcceptSyntax.defaultContinuation),
				EasyAcceptSyntax.defaultStringDelimiter,
				EasyAcceptSyntax.defaultEscapeCharacter, variables);

		setStringDelimiter(EasyAcceptSyntax.defaultStringDelimiter);
		mfReader.addMultiFileReaderObserver(plr);
		mfReader.addMultiFileReaderObserver(this);

		try {
			plr.addFile(fileName);
		} catch (FileNotFoundException e) {
			throw new FileNotFoundException("File not found: " + fileName);
		}
		if (facade == null) {
			throw new EasyAcceptException("Facade can't be null");
		}
		this.facade = facade;
		this.results = null;
		initInternalCommands();
	}

	/**
	 * Construct a Script object. This should be done for each script file to be
	 * executed.
	 * 
	 * @param fileName
	 *            The name of the file containing the test script.
	 * @param facade
	 *            The facade object giving access to the functionality of the
	 *            software to be tested
	 * @throws EasyAcceptException
	 *             if the facade object was not given.
	 * @throws FileNotFoundException
	 *             if the script file cannot be found.
	 */
	public Script(String fileName, Object facade) throws EasyAcceptException,
			FileNotFoundException, EasyAcceptInternalException {
		this(fileName, facade, new VariablesImpl());
	}

	/**
	 * This method do the request to run a script.
	 */
	public void run()  {		
		scriptExecutor();
	}
	
	/**
	 * Initialize hash map to discover internal commands quickly.
	 * 
	 * @throws EasyAcceptInternalException
	 */
	private void initInternalCommands() throws EasyAcceptInternalException {
		internalCommands = new HashMap<String, Command>();
		String command = null;
		String processor = null;
		Class procClass = null;
		Command procObject = null;
		try {
			for (int i = 0; i < EasyAcceptSyntax.internalCommandsArray.length; i++) {
				command = EasyAcceptSyntax.internalCommandsArray[i][0];
				processor = EasyAcceptSyntax.internalCommandsArray[i][1];
				procClass = Class.forName(processor);
				procObject = (Command) procClass.newInstance();
				internalCommands.put(command, procObject);
			}
		} catch (ClassNotFoundException e) {
			throw new EasyAcceptInternalException(e,
					"The class specified to process command " + command
							+ " was not found.");
		} catch (InstantiationException e) {
			throw new EasyAcceptInternalException(e,
					"The class specified to process command " + command
							+ " could not be instanciated.");
		} catch (IllegalAccessException e) {
			throw new EasyAcceptInternalException(e,
					"The class specified to process command " + command
							+ " caused IllegalAccessException.");
		}
	}

	/**
	 * Close a script. After closing, a script can be executed; it will start
	 * again. Old results are thrown out.
	 */
	public void close() throws IOException {
		plr.close();
		results = null;
	}

	/**
	 * Read and execute a single command (in sequence) from the script.
	 * 
	 * @return a {@link Result}indicating the result of the command's
	 *         execution.
	 * @throws IOException
	 *             if IO errors occur while reading the script.
	 * @throws ParsingException
	 *             if syntax errors are discovered in the script.
	 * @throws EasyAcceptException 
	 * @throws EasyAcceptException
	 *             if syntax errors are discovered in the script.
	 */
	public Result getAndExecuteCommand() throws IOException, ParsingException,
			EasyAcceptException {
		ParsedLine parsedLine = plr.getParsedLine();
		return executeCommand(parsedLine);
	}

	/**
	 * Execute the command givem by the ParsedLine object. 
	 * @param parsedLine
	 * 			The object that represents the command to be executed.
	 * @return
	 * 			The script execution result.
	 * @throws QuitSignalException
	 */
	Result executeCommand(ParsedLine parsedLine) throws QuitSignalException {
		if (parsedLine != null) {
			Throwable cause = null;
			Object result = null;
			assert parsedLine.numberOfParameters() > 0;
			try {
				if (isInternalCommand(parsedLine)) {
					result = executeInternalCommand(parsedLine);
				} else {
					result = execute(parsedLine);
				}
			} catch (InvocationTargetException ex) {
				cause = ex.getCause();
			} catch (QuitSignalException ex) {
				throw ex;
			} catch (EasyAcceptException ex) {
				cause = ex;
			} catch (IllegalAccessException ex) {
				cause = ex;
			} catch (Exception ex) {
				// should only catch the particular exceptions we're interested in
				cause = ex;
			}
			// handle variables
			String varName = parsedLine.getParameter(0).getName();
			if (varName != null && cause == null) {
				setVariable(varName, result);
			}
			return new Result(parsedLine.getCommandString(stringDelimiter),
					result, cause);
		} else {
			return null;
		}
	}

	/**
	 * Set the variable at the ParsedLineReader
	 * @param varName
	 * 				The name of the variable to be seted.
	 * @param value
	 * 				The variable's value to be seted.
	 */
	private void setVariable(String varName, Object value) {
		plr.setVariable(varName, value);
	}

	/**
	 * Execute an internal EasyAccept command by the ParsedLine given. 
	 * @param parsedLine
	 * 			The object where the command will be catch.
	 * @return
	 * 			The result of the command execution.
	 * @throws Exception
	 */
	private Object executeInternalCommand(ParsedLine parsedLine)
			throws Exception {
		Command command = (Command) internalCommands.get(parsedLine
				.getParameter(0).getValueAsString().toLowerCase());
		return command.execute(this, parsedLine);
	}
	
	/**
	 * Inform if the ParsedLine given is an internal EasyAccept command.
	 * @param parsedLine
	 * 			The object that will be verified.
	 * @return
	 * 			Return true if the ParsedLine is an internal command, otherwise returns false.
	 */
	private boolean isInternalCommand(ParsedLine parsedLine) {
		return internalCommands.containsKey(parsedLine.getParameter(0)
				.getValueAsString().toLowerCase());
	}

	/**
	 * Execute the command given by the ParsedLine to all facade's methods.
	 * @param parsedLine
	 * 			The object that represents the command.
	 * @return
	 * 			The command result execution.
	 * 
	 * @throws IllegalArgumentException
	 * @throws EasyAcceptException
	 * @throws ConverterException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	private Object execute(ParsedLine parsedLine)
			throws IllegalArgumentException, EasyAcceptException,
			ConverterException, IllegalAccessException,
			InvocationTargetException {
		assert parsedLine.numberOfParameters() > 0;
		Method[] methods = facade.getClass().getMethods();
		for (int i = 0; i < methods.length; i++) {
			if (methodMatch(methods[i], parsedLine)) {
				return methods[i].invoke(facade, parsedLine.getArgsValues());
			}
		}
		throw new EasyAcceptException("Unknown command: "
				+ parsedLine.getCommandString(stringDelimiter));
	}

	/**
	 * Try to convert the string to the expected method type. parsedLine
	 * contains the arguments values and parameters[i] contains argumet expected
	 * type.
	 */
	private boolean methodMatch(Method method, ParsedLine parsedLine)
			throws EasyAcceptException, ConverterException {

		Class[] parameters = method.getParameterTypes();
		assert parsedLine.numberOfParameters() > 0;
		if (!method.getName().equals(
				parsedLine.getParameter(0).getValueAsString())) {
			return false;
		}
		if (parameters.length != parsedLine.numberOfParameters() - 1) {
			return false;
		}
		ParameterTypeConverter.convertParam(parameters, parsedLine
				.getCommandArgs());
		return true;
	}

	/**
	 * Obtain the name of the script file being processed.
	 * @return 
	 * 			the name of the script.
	 */
	public String getFileName() {
		return plr.getCurrentFileName();
	}

	/**
	 * Execute the whole script.
	 * 
	 * @return true if there were no errors; otherwise, returns false.
	 * @throws IOException
	 *             if IO errors occur while reading the script.
	 * @throws ParsingException
	 *             if syntax errors are discovered while parsing the script.
	 * @throws EasyAcceptException
	 *             if syntax errors are discovered while trying to execute
	 *             commands, or the "quit" command was found.
	 * @throws
	 * @throws IOException
	 */
	public boolean executeAndCheck() throws EasyAcceptException, IOException,
			ParsingException {
		execute();
		return check();
	}

	/**
	 * Obtain the number of errors that occurred during script execution.
	 * @return 
	 * 			the number of errors.
	 */
	public int numberOfErrors() {
		int numberOfErrors = 0;
		Iterator it = results.iterator();
		while (it.hasNext()) {
			Result result = (Result) it.next();
			if (result.hasError()) {
				numberOfErrors++;
			}
		}
		return numberOfErrors;
	}
	
	/**
	 * Check if there ware errors during the script execution.
	 * @return
	 * 			True if there ware no erros. Otherwise, returns  false.
	 */
	public boolean check() {
		return numberOfErrors() == 0;
	}

	/**
	 * Execute the command.
	 * @throws EasyAcceptException
	 * @throws IOException
	 * @throws ParsingException
	 */
	private void execute() throws EasyAcceptException, IOException,
			ParsingException {
		this.results = new ArrayList();
		Result oneResult;
		while ((oneResult = getAndExecuteCommand()) != null) {
			results.add(oneResult);
		}
	}

	/**
	 * The results are only valid after calling  {@link #executeAndCheck}
	 * @return  a collection containing the results of the script's execution.
	 * @uml.property  name="results"
	 */
	public Collection getResults() {
		return results;
	}

	/**
	 * Obtain a formatted string containing all error messages reported during script execution.
	 * @return 
	 * 			A formatted string errors messages reported during script execution.
	 */
	public String allErrorMessages() {
		StringBuffer answer = new StringBuffer();
		Iterator it = results.iterator();
		while (it.hasNext()) {
			Result result = (Result) it.next();
			if (result.hasError()) {
				answer.append(result.getErrorMessage());
				answer.append(System.getProperty("line.separator"));
				answer.append("Command producing error: <");
				answer.append(result.getCommand());
				answer.append(">");
				answer.append(System.getProperty("line.separator"));
			}
		}
		return answer.toString();
	}

	/**
	 * Returns the line number in the script file of the last command executed.
	 * @return 
	 * 			The line number.
	 */
	public int getLineNumber() {
		return plr.getLineNumber();
	}

	/**
	 * Obtain the total number of results (see {@link #getResults}).
	 * @return 
	 * 		The total number of tests.
	 */
	public int numberOfTests() {
		return results.size();
	}

	/**
	 * Define the string delimiter for the script.
	 * @param delimiter  
	 * 			the new string delimiter.
	 * @uml.property  name="stringDelimiter"
	 */
	public void setStringDelimiter(char delimiter) {
		stringDelimiter = delimiter;
		plr.setStringDelimiter(delimiter);

	}
	/**
	 * Obtain the current ParsedLineReader object.
	 * @return
	 * 			The ParsedLineReader object.
	 */
	public ParsedLineReader getParsedLineReader() {
		return plr;
	}

	/*
	 * (non-Javadoc)
	 * @see easyaccept.util.MultiFileReaderListener#afileWasClosed(easyaccept.util.MultiFileEvent)
	 */
	public void aFileWasClosed(MultiFileEvent event) {
		restoreDefaults();
	}

	/**
	 * Set a default String Delimiter.
	 */
	private void restoreDefaults() {
		stringDelimiter = EasyAcceptSyntax.defaultStringDelimiter;
	}

	/**
	 * Returns the value of the variable names varName. Variable varName is set
	 * when a line like varName=command ... is executed.
	 * 
	 * @param varName
	 *          The name of the variable whose value is sought.
	 * @return
	 * 			The varName variable value.
	 */
	public String getVariableValue(String varName) {
		return plr.getVariableValue(varName);
	}	

	/**
	 * Returns the facade
	 * @return
	 * 			The facade object.
	 * @uml.property  name="facade"
	 */
	public Object getFacade() {
		return facade;
	}

	/**
	 * Set a facade
	 * @param  facade
	 * @uml.property  name="facade"
	 */
	public void setFacade(Object facade) {
		this.facade = facade;
	}
	
	/**
	 * This method run the current script.
	 */
	private void scriptExecutor(){
		try {			
			if (!this.executeAndCheck()) {
				System.out.println("Test file " + this.getFileName() + ": "+ this.numberOfErrors() + " errors:");
				System.out.println(this.allErrorMessages());
			} else {
				System.out.println("Test file " + getFileName() + ": "
						+ this.numberOfTests() + " tests OK");
			}
			this.close();
		} catch (QuitSignalException e) {
			if (!check()) {
				System.out.println("Test file " + getFileName() + ": "+ numberOfErrors() + " errors:");
				System.out.println(allErrorMessages());
			} else {
				System.out.println("Test file " + getFileName() + ": "+ numberOfTests() + " tests OK");
			}
			System.out.println(e.getMessage());
		} catch (EasyAcceptException e) {
			System.err.println(e.getMessage());
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParsingException e) {
			e.printStackTrace();
		}
	}

}