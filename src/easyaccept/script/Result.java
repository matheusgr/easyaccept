package easyaccept.script;

/**
 * Stores the results of a single script command after execution.
 * @author  jacques
 */
public class Result {
    /**
     * The command that was executed and produced this Result.
     */
    private String command;
    /**
     * The object returned by the command when executed.
     */
    private Object result;
    /**
     * The exception thrown by the command when executed.
     */
    private Throwable exception;
    

    /**
     * A Result constructor.
     * 
     * @param command
     *            The command that was executed and produced this Result.
     * @param result
     *            The object returned by the command when executed.
     * @param exception
     *            The exception thrown by the command when executed.
     */
    public Result(String command, Object result, Throwable exception) {
        this.command = command;
        this.result = result;
        this.exception = exception;
    }

    /**
	 * @return  A string representing the command that was executed and produced  this Result.
	 * @uml.property  name="command"
	 */
    public String getCommand() {
        return command;
    }

    /**
	 * @return  The object returned by the command when executed.
	 * @uml.property  name="result"
	 */
    public Object getResult() {
        return result;
    }

	/**
	 * @return The string equivalent of the object returned by the command when executed.
	 */
	public String getResultAsString() {
		return result == null ? "null" : result.toString();
	}

    /**
     * @return null, if no exception was thrown by the command that produced
     *         this Result; otherwise, the error message conatined in the
     *         exception that was thrown is returned.
     */
    public String getErrorMessage() {
    	if(exception == null) {
    		return "(no exception)";
    	} else if(exception.getMessage() == null) {
    		return "(no message: exception = " + exception.getClass().getName() + ")";
    	}
        return exception.getMessage();
    }

    /**
	 * @return  The exception thrown by the command when executed.
	 * @uml.property  name="exception"
	 */
    public Throwable getException() {
        return exception;
    }

    /**
     * @return true, if the command that produced this Result threw an exception
     *         when executed.
     */
    public boolean hasError() {
        return exception != null;
    }

}