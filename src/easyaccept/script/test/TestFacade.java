package easyaccept.script.test;

/**
 * A facade used in EasyAccept unit tests.
 * 
 * @author jacques
 */
public class TestFacade {
	public String commandReturningString() {
		return "result commandReturningString";
	}
	public String command1() {
		return "a=b";
	}
	public String commandReturningString(String arg) {
		return arg.toUpperCase();
	}
	
	public String commandManyParameters(String arg, String i) {
		return arg.toUpperCase()+""+i.toString();
	}	
	
	public String commandManyParametersNum(Integer arg, Integer i) {
		return arg.toString()+i.toString();
	}
	
	public String commandManyParametersPrimitive(int arg, int i) {
		int result = arg+i;
		return result+"";
	}	

	
	public int commandReturningInt(int arg) {
		return arg + 1;
	}
	
	public String decideToThrow(boolean decision) throws Exception{
		if(decision == true){
			Exception e = new Exception("error just a phrase");
			throw e;  
		}
		return "OK";
	}	
			
	public String throwAnUnexpectedException(boolean throwIt) throws Exception {
		if(throwIt) {
			throw new Exception();
		}
		return "return string";
	}
	
	public void throwException(String message) throws Exception {
		throw new Exception(message);
	}
	public boolean returnTrue() {
		return true;
	}
	public String returnParam(String param) {
		return param;
	}
}
