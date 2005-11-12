package util;

import java.util.HashMap;
import java.util.Map;

public class VariablesImpl implements Variables {

	private Map variables;
	
	public VariablesImpl() {
		this.variables = new HashMap();
	}

	public Object get(String varName) {
		return variables.get(varName);
	}

	public void put(Object variableName, Object value) {
		variables.put(variableName, value);
	}

}
