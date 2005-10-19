package easyaccept;

/**
 * This exception is responsible for implementing the quit command.
 * @author Roberta
 */
public class QuitSignalException extends EasyAcceptException {
	/**
	 * Exception constructor.
	 * 
	 * @param fileName the name of the script file being processed when the exception occurred.
	 * @param lineNumber the line number in the script file being processed when the exception occurred.
	 * @param errorMessage a description of the error that occurred.
	 */
	public QuitSignalException(String fileName, int lineNumber, String errorMessage) {
		super(fileName,lineNumber,errorMessage);
	}

	/**
	 * Exception constructor.
	 * 
	 * @param errorMessage a description of the error that occurred.
	 */
	public QuitSignalException(String errorMessage) {
		super(errorMessage);
	}

}
