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
 * @author Jacques
 * 
 * To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Generation - Code and Comments
 */
public class ParsedLine {
	private List parameters;

	public ParsedLine() {
		parameters = new ArrayList();
	}

	/**
	 * @return
	 */
	public int numberOfParameters() {
		return parameters.size();
	}

	/**
	 * @param i
	 * @return
	 */
	public Parameter getParameter(int i) {
		return (Parameter) parameters.get(i);
	}

	/**
	 * @param param
	 */
	public void addParameter(Parameter param) {
		parameters.add(param);
	}

	public void addAllParameters(Collection coll) {
		parameters.addAll(coll);
	}

	/**
	 * @return
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

		//System.out.println("Complete Value: "+ sb.toString());
		return sb.toString();
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

	/**
	 * @return
	 */
	public Object[] getArgsValues() {
		Object[] args = new Object[parameters.size() - 1];
		for (int i = 0; i < args.length; i++) {
			args[i] = ((Parameter) parameters.get(i + 1)).getValue();
		}
		return args;
	}

	public Parameter[] getCommandArgs() {
		Parameter[] args = new Parameter[parameters.size() - 1];
		for (int i = 0; i < args.length; i++) {
			args[i] = (Parameter) parameters.get(i + 1);
		}
		return args;
	}

	/**
	 * @param i
	 * @return
	 */
	public ParsedLine subLine(int skip) {
		ParsedLine sub = new ParsedLine();
		sub.addAllParameters(parameters.subList(skip, numberOfParameters()));
		return sub;
	}

	/**
	 * @return
	 */
	public Parameter[] getParameters(int from) {
		Parameter[] args = new Parameter[parameters.size() - 1];
		for (int i = from; i < args.length; i++) {
			//args[i] = parameters.get(i+1);
		}

		return (Parameter[]) parameters.toArray();
	}

}