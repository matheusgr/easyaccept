package easyaccept;


/**
 * General top-level exception for the EasyAccept package.
 * 
 * @author jacques
 */
public class EasyAcceptInternalException extends Exception {

	
	Exception exception = null;
	int	lineNumber;
	String errorMessage;
	
	/**
	 * Exception constructor.
	 * 
	 * @param fileName the name of the script file being processed when the exception occurred.
	 * @param lineNumber the line number in the script file being processed when the exception occurred.
	 * @param errorMessage a description of the error that occurred.
	 */
	public EasyAcceptInternalException(Exception e, String errorMessage) {
		this.exception = e;
		this.errorMessage = errorMessage;
	}

	/**
	 * Exception constructor.
	 * 
	 * @param errorMessage a description of the error that occurred.
	 */
	public EasyAcceptInternalException(String errorMessage) {
		super(errorMessage);
	}

}
