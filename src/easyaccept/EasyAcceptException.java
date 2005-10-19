package easyaccept;

/**
 * General top-level exception for the EasyAccept package.
 * 
 * @author jacques
 */
public class EasyAcceptException extends Exception {
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
	public EasyAcceptException(String fileName, int lineNumber, String errorMessage, Throwable cause) {
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
	public EasyAcceptException(String errorMessage) {
		super(errorMessage);
	}

	/**
	 * @param fileName2
	 * @param lineNumber2
	 * @param string
	 */
	public EasyAcceptException(String fileName, int lineNumber, String message) {
		this(fileName, lineNumber, message, null);
	}

}
