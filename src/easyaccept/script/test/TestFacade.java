package easyaccept.script.test;


/**
 * A facade used in EasyAccept unit tests.
 * 
 * @author jacques
 */
public class TestFacade {
	
	/**
	 * Return a default string that will be the test command.  
	 * @return
	 * 			The command by string.
	 */
	public String commandReturningString() {
		return "result commandReturningString";
	}
	/**
	 * Return the command1.
	 * @return
	 * 			command1
	 */
	public String command1() {
		return "a=b";
	}
	/**
	 * Return a string that will be the test command. 
	 * @param arg
	 * 			The arg that will be returned as a string command. 
	 * @return
	 * 			The command by string.
	 */
	public String commandReturningString(String arg) {
		return arg.toUpperCase();
	}
	/**
	 * Return a connected string that will be the test command. 
	 * @param arg
	 * 			An arg that will be part of the returned string command. 
	 * @param arg2
	 * 			An arg that will be part of the returned string command. 
	 * @return
	 * 			The command by string.
	 */
	public String commandManyParameters(String arg, String arg2) {
		return arg.toUpperCase()+""+arg2.toString();
	}	
	/**
	 * Receive numerical args and return a connected string that will be the test command. 
	 * @param arg
	 * 			An arg that will be part of the returned string command. 
	 * @param arg2
	 * 			An arg that will be part of the returned string command. 
	 * @return
	 * 			The command by string.
	 */
	public String commandManyParametersNum(Integer arg, Integer arg2) {
		return arg.toString()+arg2.toString();
	}
	
	/**
	 * Receive primitive int args and return a connected string that will be the test command. 
	 * @param arg
	 * 			An arg that will be part of the returned string command. 
	 * @param arg2
	 * 			An arg that will be part of the returned string command. 
	 * @return
	 * 			The command by string.
	 */
	public String commandManyParametersPrimitive(int arg, int i) {
		int result = arg+i;
		return result+"";
	}	

	/**
	 * Receive a primitive int arg and return the value plus 1. 
	 * @param arg
	 * 			An arg that will be part of the returned value. 
	 * @return
	 * 			The primitive int value.
	 */
	public int commandReturningInt(int arg) {
		return arg + 1;
	}
	
	/**
	 * Decide by the boolean decision value if an Exception will be throwed.
	 * @param decision
	 * 			A boolean that inform if the Exception will be throwed.
	 * @return
	 * 			A string informing that the Exception wasn't throwed or the method
	 * will trhow an Exception.
	 * @throws Exception
	 */
	public String decideToThrow(boolean decision) throws Exception{
		if(decision == true){
			Exception e = new Exception("error just a phrase");
			throw e;  
		}
		return "OK";
	}	
	/**
	 * Throw an unexpected Exception.
	 * @param throwIt
	 * 			The Exception to be trhowed.
	 * @return
	 * 			The trhowed Exception.
	 * @throws Exception
	 */		
	public String throwAnUnexpectedException(boolean throwIt) throws Exception {
		if(throwIt) {
			throw new Exception();
		}
		return "return string";
	}
	/**
	 * Trhown an Exception with a massage passed.
	 * @param message
	 * 			The message that will be showed by the Ecxeption.
	 * @throws Exception
	 */
	public void throwException(String message) throws Exception {
		throw new Exception(message);
	}
	/**
	 * The method return true.
	 * @return
	 * 			true.
	 */
	public boolean returnTrue() {
		return true;
	}
	/**
	 * Return the received parameter
	 * @param param
	 * 			The received parameter.
	 * @return
	 * 			The parameter.
	 */
	public String returnParam(String param) {
		return param;
	}
	
}