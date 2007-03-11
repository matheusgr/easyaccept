/*
 * Created on 10/06/2004
 * 
 * To change the template for this generated file go to Window - Preferences -
 * Java - Code Generation - Code and Comments
 */
package util;

/**
 * @author  Jacques  
 */
public class Parameter {
	
	private String name;
	private Object value;
	/**
	 * The Parameter constructor.
	 * @param name
	 * 			The parameter name.
	 * @param value
	 * 			The parameter value.
	 */
	public Parameter(String name, Object value) {
		this.name = name;
		this.value = value;
	}

	/**
	 * Obtain the parameter name
	 * @return
	 * 			The name.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Obtain the parameter value
	 * @return
	 * 			The value.
	 */
	public Object getValue() {
		return value;
	}
	/**
	 * Obtain the parameter value as string.
	 * @return
	 * 			The value as string.
	 */
	public String getValueAsString() {
		return value == null ? "null" : value.toString();
	}

	/**
	 * Configure the parameter value. 
	 * @param p_value
	 * 			The new value.
	 */
	public void setValue(Object p_value) {
		this.value = p_value;
	}
	
	/**
	 * Obtain the parameter as string.
	 */
	public String toString() {
		return toString('\"');
	}

	/**
	 * Obtain the parameter as string using a delimiter
	 * @param delimiter
	 * 				The delimiter
	 * @return
	 * 				The pramter as string  
	 */
	public String toString(char delimiter) {
		String str = "";
		if (getName() != null) {
			str += printableString(getName(), delimiter) + "=";
		}
		if (getValue() != null) {
			str += printableString(getValueAsString(), delimiter);
		}
		return str;
	}

	/**
	 * Obtain a printable string
	 * @param stringDelimiter
	 * 			The stringDelimiter used.
	 * @param valueAsString
	 * 			The valueAsString used.
	 * @return
	 * 			The printable string.
	 */
	private String printableString(String string, char stringDelimiter) {
		if (string.indexOf(" ") >= 0) {
			return stringDelimiter + string + stringDelimiter;
		} else {
			return string;
		}
	}
	/**
	 * Obatain the parameter value as double
	 * @return
	 * 			The value as double.
	 */
	public double getValueAsDouble() {
		if(value instanceof Double) {
			return ((Double)value).doubleValue();
		} else {
			return Double.parseDouble(getValueAsString());
		}
	}
	/**
	 * Obatain the parameter value as int
	 * @return
	 * 			The value as int.
	 */
	public int getValueAsInt(){
		if(value instanceof Integer) {
			return ((Integer)value).intValue();
		} else {
			return Integer.parseInt(getValueAsString());
		}
	}
}