/*
 * Created on Oct 14, 2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package util;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;

/**
 * Provide the parameter type comverter.
 * @author roberta
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class ParameterTypeConverter {

	private static HashMap primitiveWraper = new HashMap();
	private static Class classString[] = {java.lang.String.class};
	
	static{
		primitiveWraper.put("boolean",java.lang.Boolean.class);  
		primitiveWraper.put("char",java.lang.Character.class);  
		primitiveWraper.put("byte", java.lang.Byte.class);  
		primitiveWraper.put("short",java.lang.Short.class);
		primitiveWraper.put("int",java.lang.Integer.class);  
		primitiveWraper.put("long",java.lang.Long.class);  
		primitiveWraper.put("float",java.lang.Float.class);
		primitiveWraper.put("double",java.lang.Double.class);
	}
	
	/**
	 * Execute the parameter type comverter. 
	 * @param facadeParam
	 * 				The facade parameter used.
	 * @param userDefParam
	 * 				The user defined parameter.
	 * @throws ConverterException
	 */
	public static void convertParam(Class[] facadeParam, Parameter[] userDefParam) throws ConverterException{
    	 
    	String argValue[] = new String[1]; 
    	Class paramClass = null;
    	String argType = null;
    	Class argTypeClassTmp = null;
		Object convertedArg;
    	for (int i = 0; i < facadeParam.length; i++) {
    		
    		paramClass = facadeParam[i];
            argValue[0] = userDefParam[i].getValueAsString();
            // Gets the parameter type defined in facade
            argType = paramClass.getName();      
        	if ( !argType.equals("java.lang.String")) {
            	Constructor c;
            	try {
            		argTypeClassTmp = (Class) primitiveWraper.get(argType);
            		if (argTypeClassTmp != null){
            			paramClass = argTypeClassTmp;
            		}
            		c = paramClass.getConstructor(classString);
            		convertedArg = c.newInstance(argValue);
					userDefParam[i].setValue(convertedArg);            	
            	} catch (SecurityException e) {
            		throw new ConverterException ("Problems during Type Conversion - " + argValue[0] +" to " + paramClass.toString() );
				} catch (NoSuchMethodException e) {
					throw new ConverterException ("Problems during Type Conversion - " + argValue[0] +" to " + paramClass.toString() );
				} catch (IllegalArgumentException e) {
					throw new ConverterException ("Problems during Type Conversion - " + argValue[0] +" to " + paramClass.toString());
				} catch (InstantiationException e) {
					throw new ConverterException ("Problems during Type Conversion - " + argValue[0] +" to " + paramClass.toString());
				} catch (IllegalAccessException e) {
					throw new ConverterException ("Problems during Type Conversion - " + argValue[0] +" to " + paramClass.toString());
				} catch (InvocationTargetException e) {
					throw new ConverterException ("Problems during Type Conversion - " + argValue[0] +" to " + paramClass.toString());
				}
			}//if    
        }//for  	
     }
}
