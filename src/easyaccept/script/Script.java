/*
 * Project: topogiggio
 * Script.java 
 *
 * Copyright 2004 Universidade federal da Campina Grande. All rights reserved.
 */

package easyaccept.script;

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
import easyaccept.EasyAcceptException;
import easyaccept.EasyAcceptInternalException;
import easyaccept.EasyAcceptSyntax;
import easyaccept.QuitSignalException;

/**
 * The <code>Script</code> class must be instantiated for each test script to
 * be executed. A test script is a sequence of commands present in a test file.
 * A script may consist of two types of commands: internal and external. The
 * internal commands are part of EasyAccept itself. So far, these commands
 * include:
 * <p>
 * <blockquote>
 * 
 * <pre>
 * 
 *  
 *   
 *    
 *     
 *      
 *           expect &quot;string&quot; command ...
 *           stringdelimiter delimiter
 *       
 *      
 *     
 *    
 *   
 *  
 * </pre>
 * 
 * </blockquote>
 * <p>
 * Each of these internal commands is explained in the corresponding
 * "xxxProcessor" class.
 * <p>
 * The external commands are those available through the facade providing access
 * to the functionality of the software being tested. Basically, any public
 * method available in the facade can be called in the script. The return value
 * can then be tested (using an internal command such as <code>expect</code>).
 * <p>
 * Once a script is created, it can be executed, either one command at a time or
 * all commands at once. Results can be obtained about the execution of a
 * script's commands.
 * 
 * @author jacques
 */
public class Script implements MultiFileReaderObserver {

	/**
	 * Table to check if a command is an internal command.
	 */
	private static Map internalCommands;

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
	private Collection results;

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
	public Script(String fileName, Object facade, Map variables)
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
		this(fileName, facade, new HashMap());
	}

	/**
	 * Initialize hash map to discover internal commands quickly.
	 * 
	 * @throws EasyAcceptInternalException
	 */
	private void initInternalCommands() throws EasyAcceptInternalException {
		internalCommands = new HashMap();
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
	 * @throws QuitSignalException
	 *             if 'quit' command was found
	 * 
	 * @throws EasyAcceptException
	 *             if syntax errors are discovered in the script.
	 */
	public Result getAndExecuteCommand() throws IOException, ParsingException,
			QuitSignalException {
		ParsedLine parsedLine = plr.getParsedLine();
		return executeCommand(parsedLine);
	}

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
				//            	//TODO (todolist) Not nice to use catch Exception, too
				// dangerous
				// should only catch the particular exceptions we're interested
				// in
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
	 * @param varName
	 * @param result
	 */
	private void setVariable(String varName, Object value) {
		plr.setVariable(varName, value);
	}

	private Object executeInternalCommand(ParsedLine parsedLine)
			throws Exception {
		Command command = (Command) internalCommands.get(parsedLine
				.getParameter(0).getValueAsString().toLowerCase());

		return command.execute(this, parsedLine);
	}

	private boolean isInternalCommand(ParsedLine parsedLine) {
		return internalCommands.containsKey(parsedLine.getParameter(0)
				.getValueAsString().toLowerCase());
	}

	private Object execute(ParsedLine parsedLine)
			throws IllegalArgumentException, EasyAcceptException,
			ConverterException, IllegalAccessException,
			InvocationTargetException {
		assert parsedLine.numberOfParameters() > 0;
		//		System.err.println("class " + facade.getClass().getName());
		Method[] methods = facade.getClass().getMethods();
		for (int i = 0; i < methods.length; i++) {
			if (methodMatch(methods[i], parsedLine)) {
				//				System.err.println("match");
				//				try {
				return methods[i].invoke(facade, parsedLine.getArgsValues());
				//				} catch(InvocationTargetException ex) {
				//					throw ex.getCause();
				//				}
			}
		}
		throw new EasyAcceptException("Unknown command: "
				+ parsedLine.getCommandString(stringDelimiter));
	}

	/*
	 * we will try to convert the string to the expected method type. parsedLine
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
			//						System.err.println("no match: number of parameters");
			return false;
		}

		ParameterTypeConverter.convertParam(parameters, parsedLine
				.getCommandArgs());

		return true;
	}

	/**
	 * @return the name of the script file being processed.
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
	 * @return the number of errors that occurred during script execution
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

	public boolean check() {
		return numberOfErrors() == 0;
	}

	private void execute() throws EasyAcceptException, IOException,
			ParsingException {
		this.results = new ArrayList();
		Result oneResult;
		while ((oneResult = getAndExecuteCommand()) != null) {
			results.add(oneResult);
		}
	}

	/**
	 * The results are only valid after calling {@link #executeAndCheck}
	 * 
	 * @return a collection containing the results of the script's execution.
	 */
	public Collection getResults() {
		return results;
	}

	/**
	 * @return A formatted string containing all error messages reported during
	 *         script execution.
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

				//                answer.append("Command: <");
				//                answer.append(result.getCommand());
				//                answer.append(">, produced error: <");
				//                answer.append(result.getErrorMessage());
				//                answer.append(">");
				//                answer.append(System.getProperty("line.separator"));
			}
		}
		return answer.toString();
	}

	/**
	 * @return The line number in the script file of the last command executed.
	 */
	public int getLineNumber() {
		return plr.getLineNumber();
	}

	/**
	 * @return The total number of results (see {@link #getResults}).
	 */
	public int numberOfTests() {
		return results.size();
	}

	/**
	 * Define the string delimiter for the script.
	 * 
	 * @param delimiter
	 *            the new string delimiter.
	 */
	public void setStringDelimiter(char delimiter) {
		stringDelimiter = delimiter;
		plr.setStringDelimiter(delimiter);

	}

	public ParsedLineReader getParsedLineReader() {
		return plr;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see easyaccept.util.MultiFileReaderListener#afileWasClosed(easyaccept.util.MultiFileEvent)
	 */
	public void aFileWasClosed(MultiFileEvent event) {
		//System.out.println("Notified:" +
		// ((MultiFileReader)event.getSource()).getCurrentFileName() );
		restoreDefaults();
	}

	private void restoreDefaults() {
		stringDelimiter = EasyAcceptSyntax.defaultStringDelimiter;
	}

	/**
	 * Returns the value of the variable names varName. Variable varName is set
	 * when a line like varName=command ... is executed.
	 * 
	 * @param varName
	 *            The name of the variable whose value is sought
	 * @return
	 */
	public String getVariableValue(String varName) {
		return plr.getVariableValue(varName);
	}
}