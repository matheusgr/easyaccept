package util;

/**
 * Define the parsing Exception.
 * @author jacques
 */
public class ParsingException extends Exception {
	String fileName;
	int	lineNumber;
	String errorMessage;
	
	/**
	 * Exception constructor.
	 * 
	 * @param fileName the name of the script file being processed when the exception occurred.
	 * @param lineNumber the line number in the script file being processed when the exception occurred.
	 * @param errorMessage a description of the error that occurred.
	 */
	public ParsingException(String fileName, int lineNumber, String errorMessage, Throwable cause) {
		super("Line " + lineNumber + ", file " + fileName 
				+ ": " + errorMessage, cause);
		this.fileName = fileName;
		this.lineNumber = lineNumber;
		this.errorMessage = errorMessage;
	}

	/**
	 * Exception constructor.
	 * 
	 * @param errorMessage a description of the error that occurred.
	 */
	public ParsingException(String errorMessage) {
		super(errorMessage);
	}

	/**
	 * @param fileName2
	 * @param lineNumber2
	 * @param string
	 */
	public ParsingException(String fileName, int lineNumber, String message) {
		this(fileName, lineNumber, message, null);
	}

}
