/*
 * Created on 22/08/2004
 *
 */
package util;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * The stringUtil class provide the string treatment.
 * @author jacques
 * 
 */
public class StringUtil {
	/**
	 * Remove a new string line
	 * @param line
	 * 			The line to be removed
	 * @return
	 * 			The treated line.
	 */
	public static String removeNewLine(String line) {
		if (line != null && line.charAt(line.length() - 1) == '\n') {
			return line.substring(0, line.length() - 1);
		} else {
			return line;
		}
	}

	/**
	 * Remove a new line from the stringBuffer passad
	 * @param lineBuffer
	 * 			The string buffer.
	 * @return
	 * 			The treated stringBuffer.
	 */
	public static StringBuffer removeNewline(StringBuffer lineBuffer) {
		if (lineBuffer.charAt(lineBuffer.length() - 1) == '\n') {
			lineBuffer.deleteCharAt(lineBuffer.length() - 1);
		}
		return lineBuffer;
	}

	/**
	 * 
	 * @param exception
	 * @return
	 */
	public static String exceptionToString(Throwable exception) {
		StringWriter strWriter = new StringWriter();
		PrintWriter printWriter = new PrintWriter(strWriter);
		do {
			printWriter.println();
			exception.printStackTrace(printWriter);
			exception = exception.getCause();
		} while(exception != null);
		printWriter.flush();
		strWriter.flush();
		return strWriter.toString();
	}
	/**
	 * @param valueAsString
	 * @param resultAsString
	 * @return
	 */
	public static boolean areEqual(String s1, String s2) {
		return s1 == null && s2 == null ||
			s1 != null && s1.equals(s2);
	}
}