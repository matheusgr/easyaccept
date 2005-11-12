/*
 * Created on 10/06/2004
 * 
 * To change the template for this generated file go to Window - Preferences -
 * Java - Code Generation - Code and Comments
 */
package util;

import java.io.IOException;

/**
 * @author Jacques
 * 
 * To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Generation - Code and Comments
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

	public ParsedLineReader(Stream stream, char p_defaultStringDelimiter,
			char p_escape, Variables variables) {
		super(stream);
		stringDelimiter = defaultStringDelimiter = p_defaultStringDelimiter;
		escape = defaultEscape = p_escape;
		this.variables = variables;
	}

	public ParsedLineReader(Stream stream) {
		this(stream, defaultStringDelimiter, defaultEscape);
	}

	public ParsedLineReader(Stream stream, char stringDelimiter, char escape) {
		this(stream, stringDelimiter, escape, makeVariables());
	}

	private static Variables makeVariables() {
		return new VariablesImpl();
	}

	public ParsedLineReader(Stream stream, Variables variables) {
		this(stream, defaultStringDelimiter, defaultEscape, variables);
	}

	public ParsedLine getParsedLine() throws IOException, ParsingException {
		ParsedLine parsedLine = null;
		Parameter param;
		//		skipEmptyLines();
		while ((param = getParameter()) != null) {
			if (parsedLine == null) {
				parsedLine = new ParsedLine();
			}
			parsedLine.addParameter(param);
		}
		int c = read(); // read the end-of-line character
		return parsedLine;
	}

	/**
	 * @return
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
	 * @return
	 * @throws IOException
	 * @throws ParsingException
	 */

	private String getToken() throws IOException, ParsingException {
		StringBuffer sb = null;

		int c = read();
		while (c >= 0 && !Character.isWhitespace((char) c) && c != '=') {

			//System.out.println("->"+(char) c);

			if (sb == null) {
				sb = new StringBuffer();
			}

			if ((char) c == stringDelimiter) {
				sb.append(parseString(stringDelimiter));
				//read next
				c = read();

			} else if ((char) c == escape) {

				//System.out.println("Escape seen!");
				c = read();
				if (c >= 0) {
					sb.append((char) c);
					//System.out.println("***" + sb.toString());
					c = read();
				}

			} else if ((char) c == variableStart) {
				c = read(); // read {
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
			// character will be part of next token
			//System.err.println("Unread::"+ (char) c);
			unread(c);
		}
		//System.err.println("returning token " + (sb == null ? null :
		// sb.toString()));
		return sb == null ? null : sb.toString();
	}

	/**
	 * @param varName
	 * @return
	 */
	public String getVariableValue(String varName) {
		Object var = variables.get(varName); 
		return var == null ? null : var.toString();
	}

	/**
	 * @param endDelim
	 * @return
	 * @throws IOException
	 * @throws ParsingException
	 */
	private String parseString(char endDelimiter) throws IOException,
			ParsingException {
		StringBuffer sb = new StringBuffer();
		int c;
		while ((c = read()) >= 0 && c != '\n' && c != endDelimiter) {
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
	 *  
	 */
	private void skipWhiteSpace() throws IOException {
		int carac;
		while ((carac = read()) >= 0 && (carac == ' ' || carac == '\t')) {
			//			System.err.println("skip" + (char)carac + "(" + carac + ")");
		}
		if (carac >= 0) {
			//			System.err.println("unread " + (char)carac);
			unread(carac);
		}
	}

	/**
	 * @param c
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

	private void restoreDefaults() {
		stringDelimiter = defaultStringDelimiter;
	}

	public void setEscape(char p_escape) {
		this.escape = defaultEscape = p_escape;
	}

	public char getEscape() {
		return this.escape;
	}

	public void setVariable(String variableName, Object value) {
		variables.put(variableName, value);
	}

}