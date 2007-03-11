/*
 * Created on 10/06/2004
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * The ParsedLine class. 
 * @author Jacques
 * 
 * To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Generation - Code and Comments
 */
public class ParsedLine {
	private List<Parameter> parameters;
	
	/**
	 * Construct a ParsedLine object. 
	 */
	public ParsedLine() {
		parameters = new ArrayList<Parameter>();
	}

	/**
	 * Returns the ParsedLine number of parameters. 
	 * @return
	 * 			The number of parameters.
	 */
	public int numberOfParameters() {
		return parameters.size();
	}

	/**
	 * Get a parameter by the parameters list.
	 * @param aParameter
	 * 			The parameter posicion to be back. 
	 * @return
	 * 			A parameter.
	 */
	public Parameter getParameter(int aParameter) {
		return (Parameter) parameters.get(aParameter);
	}

	/**
	 * Add an parameter to the parameters list.
	 * @param param
	 * 			The parameter to be added.
	 */
	public void addParameter(Parameter param) {
		parameters.add(param);
	}

	/**
	 * Add a parameters collection to the parameters list.
	 * @param coll
	 * 			The parameters collection to be added.
	 */
	public void addAllParameters(Collection coll) {
		parameters.addAll(coll);
	}

	/**
	 * Obtain the command by a string.  
	 * @return
	 * 			The string command.
	 */
	public String getCommandString(char stringDelimiter) {
		StringBuffer sb = new StringBuffer();
		Iterator it = parameters.iterator();
		while (it.hasNext()) {
			Parameter param = (Parameter) it.next();
			sb.append(param.toString(stringDelimiter));
			if (it.hasNext()) {
				sb.append(" ");
			}
		}
		return sb.toString();
	}

	/**
	 * Obtain the ParsedLine args values.
	 * @return
	 * 		The args values.
	 */
	public Object[] getArgsValues() {
		Object[] args = new Object[parameters.size() - 1];
		for (int i = 0; i < args.length; i++) {
			args[i] = ((Parameter) parameters.get(i + 1)).getValue();
		}
		return args;
	}

	/**
	 * Obtain the command args.
	 * @return
	 * 			The args.
	 */
	public Parameter[] getCommandArgs() {
		Parameter[] args = new Parameter[parameters.size() - 1];
		for (int i = 0; i < args.length; i++) {
			args[i] = (Parameter) parameters.get(i + 1);
		}
		return args;
	}

	/**
	 * 
	 * @param skip
	 * @return
	 */
	public ParsedLine subLine(int skip) {
		ParsedLine sub = new ParsedLine();
		sub.addAllParameters(parameters.subList(skip, numberOfParameters()));
		return sub;
	}

}