package util;

import java.util.HashMap;
import java.util.Map;

/**
 * A VariablesImpl object provides the Map to hold script variables that will be executed.
 * Besides, the VariablesImpl object provides get and put methods.
 * @author Win XP
 */
public class VariablesImpl implements Variables {

	private Map variables;
	/**
	 * Construct a VariablesImpl object.
	 */
	public VariablesImpl() {
		this.variables = new HashMap();
	}
	/**
	 * Returns a variable with the giving varName.
	 */
	public Object get(String varName) {
		return variables.get(varName);
	}
	/**
	 * Put a variable object into the VariablesImpl Map with the especified value.
	 */
	public void put(Object variableName, Object value) {
		variables.put(variableName, value);
	}

}
