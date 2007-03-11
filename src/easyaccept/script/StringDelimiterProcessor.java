package easyaccept.script;

import util.ParsedLine;
import easyaccept.EasyAcceptException;

/**
 * A single object of this class is instantiated to handle all script calls to
 * the <code>stringdelimiter</code> internal command. The
 * <code>stringdelimiter</code> command has syntax:
 * <p>
 * <blockquote>
 * 
 * <pre>
 *       stringdelimiter delimiter
 * </pre>
 * 
 * </blockquote>
 * <p>
 * It sets the indicated delimiter as the string quoting character. String
 * quoting is necessary when a string includes white space.
 * 
 * @author jacques
 */
public class StringDelimiterProcessor implements Command {
    /**
     * Executes the <code>stringdelimiter</code> internal command.
     * 
     * @param script
     *            the script being executed serving as context for the
     *            <code>stringdelimiter</code> internal command.
     * @param parsedLine
     *            the script line being executed. This line consists of the
     *            <code>stringdelimiter</code> keyword, followed by 
     *            string quoting character.
     * @return "OK" if the command executes correctly.
     * @throws EasyAcceptException
     *             if a syntax error is detected; or if a single
     *             quoting character is not provided.
     * @see easyaccept.script.Command#execute(easyaccept.script.Script,
     *      easyaccept.util.ParsedLine)
     */
    public Object execute(Script script, ParsedLine parsedLine)
            throws EasyAcceptException {
    	
    	int beginning = 0;
    	String delimiter = "";
    	if(parsedLine.numberOfParameters() == 2) {
    		delimiter = parsedLine.getParameter(1).getValueAsString();
    	}
    	if (parsedLine.numberOfParameters() != 2 || delimiter.length() != 1) {
            throw new EasyAcceptException(script.getFileName(), script
                    .getLineNumber(),
                    "Syntax error: stringdelimiter <character_delimiter>");
        }
    	script.setStringDelimiter(delimiter.charAt(beginning));
        return "OK";
    }
}