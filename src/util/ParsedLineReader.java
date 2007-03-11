/*
 * Created on 10/06/2004
 * 
 * To change the template for this generated file go to Window - Preferences -
 * Java - Code Generation - Code and Comments
 */
package util;

import java.io.IOException;

/**
 * The parsedLineReader class represents a script line to be read.
 * @author  Jacques  To change the template for this generated type comment go to Window -  Preferences - Java - Code Generation - Code and Comments
 */
public class ParsedLineReader extends AdapterStream implements
		MultiFileReaderObserver {

	private char stringDelimiter;
	private static char defaultStringDelimiter = '"';
	//If the user places a back slash ("\") in front of a character, such
	// as: escapes, quotes and the end of line character, it no longer has a special
	// meaning.
	private char escape;
	private static char defaultEscape = '\\';
	private Variables variables;
	private char variableStart = '$';
	
	/**
	 * Construct an ParsedLineReader object with four parameters.
	 * @param stream 
	 * 				The stream to be processed.
	 * @param p_defaultStringDelimiter 
	 * 				The used string delimiter.
	 * @param p_escape 
	 * 				The determinated escape string.
	 * @param variables 
	 * 				The variables Map. 
	 */
	public ParsedLineReader(Stream stream, char p_defaultStringDelimiter,
			char p_escape, Variables variables) {
		super(stream);
		stringDelimiter = defaultStringDelimiter = p_defaultStringDelimiter;
		escape = defaultEscape = p_escape;
		this.variables = variables;
	}
	
	/**
	 * Construct an ParsedLineReader object with only one parameter.
	 * @param stream 
	 * 				The stream to be processed.
	 */
	public ParsedLineReader(Stream stream) {
		this(stream, defaultStringDelimiter, defaultEscape);
	}
	
	/**
	 * Construct an ParsedLineReader object with three parameters.
	 * @param stream
	 * 				The stream to be processed.
	 * @param stringDelimiter 
	 * 				The used string delimiter.
	 * @param escape
	 * 				The determinated escape string.
	 */
	public ParsedLineReader(Stream stream, char stringDelimiter, char escape) {
		this(stream, stringDelimiter, escape, makeVariables());
	}
	
	/**
	 * Construct an ParsedLineReader object with two parameters.
	 * @param stream
	 * 				The stream to be processed.
	 * @param variables
	 * 				The variables Map. 
	 */
	public ParsedLineReader(Stream stream, Variables variables) {
		this(stream, defaultStringDelimiter, defaultEscape, variables);
	}


	/**
	 * Create a variables Map.
	 * @return
	 * 			An empty variables Map.
	 */
	private static Variables makeVariables() {
		return new VariablesImpl();
	}

	/**
	 * Obtain a ParsedLine object after adding it parameters.
	 * @return
	 * 			The ParsedLine.
	 * @throws IOException
	 * @throws ParsingException
	 */
	public ParsedLine getParsedLine() throws IOException, ParsingException {
		ParsedLine parsedLine = null;
		Parameter param;
		//skipEmptyLines();
		int i = 0;
		while ((param = getParameter()) != null) {
			i++;
			if (parsedLine == null) {
				parsedLine = new ParsedLine();
			}
			parsedLine.addParameter(param);
		}
		//read the end-of-line character
		read();
		return parsedLine;
	}

	/**
	 * Obtain a Parameter to be added at the ParsedLine object.
	 * @return
	 * 			The Parameter object.
	 * @throws IOException
	 * @throws ParsingException
	 */
	private Parameter getParameter() throws IOException, ParsingException {
		Parameter param = null;
		skipWhiteSpace();
		String name = getToken();
		if (name != null) {
			Object value = null;
			int nextChar = read();
			if (nextChar == '=') {
				value = getToken();
			} else {
				value = name;
				name = null;
				if (nextChar >= 0) {
					unread(nextChar);
				}
			}
			param = new Parameter(name, value);
		}
		return param;
	}

	/**
	 * Obtain a Token
	 * @return
	 * 			The StringBuffer referring to the Token.
	 * @throws IOException
	 * @throws ParsingException
	 */
	private String getToken() throws IOException, ParsingException {
		
		StringBuffer sb = null;
		int c = read();
		while (c >= 0 && !Character.isWhitespace((char) c) && c != '=') {
			if (sb == null) {
				sb = new StringBuffer();
			}
			if ((char) c == stringDelimiter) {
				sb.append(parseString(stringDelimiter));
				c = read();
			} else if ((char) c == escape) {
				c = read();
				if (c >= 0) {
					sb.append((char) c);
					c = read();
				}
			} else if ((char) c == variableStart) {
				c = read();
				if (c >= 0) {
					if ((char) c == '{') {
						String varName = parseString('}');
						String varValue = getVariableValue(varName);
						if (varValue != null) {
							sb.append(varValue);
						}
					} else {
						sb.append(variableStart);
						sb.append((char) c);
					}
					c = read();
				} else {
					sb.append(variableStart);
					c = read();
				}
			} else {
				sb.append((char) c);
				c = read();
			}
		}
		if (c >= 0) {
			unread(c);
		}
		return sb == null ? null : sb.toString();
	}
	

	/**
	 * Obtain by the variables Map variable's value by it's name.
	 * @param varName
	 * 			The variable name.
	 * @return
	 * 			The variable object.
	 */
	public String getVariableValue(String varName) {
		Object var = variables.get(varName); 
		return var == null ? null : var.toString();
	}

	/**
	 * The method returns the parseString read by the current stream. 
	 * @param endDelim
	 * 			The used string delimiter.	
	 * @return
	 * 			The string read by the stream.
	 * @throws IOException
	 * @throws ParsingException
	 */
	private String parseString(char endDelimiter) throws IOException,
			ParsingException {
		StringBuffer sb = new StringBuffer();
		int c;
		while ((c = read()) >= 0 && c != '\n' && c != endDelimiter) {
			if(c == escape) {
				c = read();
			}
			sb.append((char) c);
		}
		if (c != endDelimiter) {
			throw new ParsingException(getCurrentFileName(), getLineNumber(),
					"Missing end-delimiter character :: " + endDelimiter
							+ " -- " + (char) c + " -- ");
		}
		if (c >= 0 && c != endDelimiter) {
			unread(c);
		}
		return sb.toString();
	}

	/**
	 * The method skip white spaces found at the current stream.
	 */
	private void skipWhiteSpace() throws IOException {
		int carac;
		while ((carac = read()) >= 0 && (carac == ' ' || carac == '\t')) {
		}
		if (carac >= 0) {
			unread(carac);
		}
	}

	/**
	 * Set the current string delimiter by other one known by parameter.
	 * @param  c
	 * 			The new string delimiter.
	 * @uml.property  name="stringDelimiter"
	 */
	public void setStringDelimiter(char c) {
		stringDelimiter = c;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see easyaccept.util.MultiFileReaderListener#afileWasClosed(easyaccept.util.MultiFileEvent)
	 */
	public void aFileWasClosed(MultiFileEvent event) {
		restoreDefaults();
	}

	/**
	 * Restore the deafult string delimiter.
	 */
	private void restoreDefaults() {
		stringDelimiter = defaultStringDelimiter;
	}

	/**
	 * Set the default escape. 
	 * @param escape  
	 * 			The escape to set.
	 * @uml.property  name="escape"
	 */
	public void setEscape(char p_escape) {
		this.escape = defaultEscape = p_escape;
	}

	/**
	 * Get the current escape.
	 * @return  
	 * 			Returns the escape.
	 * @uml.property  name="escape"
	 */
	public char getEscape() {
		return this.escape;
	}
	/**
	 * Put at the variables Map a variable given by the name and value by parameters. 
	 * @param variableName
	 * 			The variables name.
	 * @param value
	 * 			The variables value.
	 */
	public void setVariable(String variableName, Object value) {
		variables.put(variableName, value);
	}

}
