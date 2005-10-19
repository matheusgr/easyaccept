/*
 * Created on Oct 14, 2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package util;

import java.lang.reflect.Method;

import junit.framework.TestCase;

/**
 * @author roberta
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class TestParameterTypeConverter extends TestCase {

	String facadeName = "util.TestParameterTypeConverter";
	Object facade = null;
	Method[] methods = null;
	Parameter[] parametersA = new Parameter[2];
	Parameter[] parametersB = new Parameter[2];
	Parameter[] parametersBool = new Parameter[1];
	
    public void setUp(){
        	
    	try {
			Class facadeClass = Class.forName(facadeName);
			facade = facadeClass.newInstance();
		} catch (ClassNotFoundException e1) {
			System.err.println("Facade not found: " + facadeName);
		} catch (InstantiationException e) {
			System.err.println("Facade not found: " + facadeName);
		} catch (IllegalAccessException e) {
			System.err.println("Facade not found: " + facadeName);
		} 
    	
		methods = facade.getClass().getMethods();
		parametersA[0] = new Parameter("arg1","13");
		parametersA[1] = new Parameter("arg1","1230");
				
		parametersB[0] = new Parameter("arg1","ARG");
		parametersB[1] = new Parameter("arg1","ARG");
		
		parametersBool[0] = new Parameter("arg1","true");
		
    } 	

	public void testConvertParam() {
		for (int i = 0; i < methods.length; i++) {
			
		
			if(methods[i].getName().equals("commandManyParameters")){
				try {
					ParameterTypeConverter.convertParam( methods[i].getParameterTypes(), parametersA );
				} catch (ConverterException e) {
					fail();
				}
				assertEquals("java.lang.Integer", parametersA[0].getValue().getClass().getName());
				try {
					ParameterTypeConverter.convertParam( methods[i].getParameterTypes(), parametersB );
					fail();
				} catch (ConverterException e) {
					//the exection must be thrown
					assertEquals("util.ConverterException", e.getClass().getName());
				}
			}//if
			
			
			if(methods[i].getName().equals("commandManyParametersNum")){
				try {
					ParameterTypeConverter.convertParam( methods[i].getParameterTypes(), parametersA );
				} catch (ConverterException e) {
					e.printStackTrace();
					fail();
				}
				assertEquals("java.lang.Integer", parametersA[0].getValue().getClass().getName());
				assertEquals("java.lang.Long", parametersA[1].getValue().getClass().getName());
				
				try {
					ParameterTypeConverter.convertParam( methods[i].getParameterTypes(), parametersB );
					fail();
				} catch (ConverterException e) {
					//the exection must be thrown
					assertEquals("util.ConverterException", e.getClass().getName());
				}
			}//if
			
			
			if(methods[i].getName().equals("commandBoolean")){
				try {
					ParameterTypeConverter.convertParam( methods[i].getParameterTypes(), parametersBool );
				} catch (ConverterException e) {
					e.printStackTrace();
					fail();
				}
				assertEquals("java.lang.Boolean", parametersBool[0].getValue().getClass().getName());
				
			}//if
				
			
		}//for		
		
	}

	public String commandManyParameters(Integer arg, String i) {
		return arg.toString()+i.toString();
	}
	
	public String commandManyParametersNum(int arg, long i) {
		return arg+""+i;
	}
	
	public String commandBoolean(boolean arg) {
		return arg?"OK":"FAIL";
	}
	
}
