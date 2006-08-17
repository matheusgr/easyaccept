/*
 * Created on 10/06/2004
 * 
 * To change the template for this generated file go to Window - Preferences -
 * Java - Code Generation - Code and Comments
 */
package util;


/**
 * @author Jacques
 * 
 * To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Generation - Code and Comments
 */
public class Parameter {
	private String name;

	private Object value;

	public Parameter(String name, Object value) {
		//		System.err.println("creating Parameter(" + name + "," + value + ")");
		this.name = name;
		this.value = value;
	}

	/**
	 * @return
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return
	 */
	public Object getValue() {
		return value;
	}

	public String getValueAsString() {
		return value == null ? "null" : value.toString();
	}

	public double getValueAsDouble() {
		if(value instanceof Double) {
			return ((Double)value).doubleValue();
		} else {
			return Double.parseDouble(getValueAsString());
		}
	}

	/**
	 * @return
	 */
	public void setValue(Object p_value) {
		this.value = p_value;
	}

	public String toString() {
		return toString('\"');
	}

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
	 * @param stringDelimiter
	 * @param valueAsString
	 * @return
	 */
	private String printableString(String string, char stringDelimiter) {
		if (string.indexOf(" ") >= 0) {
			return stringDelimiter + string + stringDelimiter;
		} else {
			return string;
		}
	}

}