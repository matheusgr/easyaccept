package easyaccept;

/**
 * The <code>EasyAcceptSyntax</code> class represents details of the 
 * EasyAccept syntax, as well it's internal commands.
 * 
 * @author roberta
 */

public class EasyAcceptSyntax {
	
	/**
	 * The character serving as default comment character in script files
	 */
	public static final char defaultComment = '#';

	/**
	 * The character serving as default line continuation character in script
	 * files
	 */
	public static final char defaultContinuation = '\\';

	public static final char defaultEscapeCharacter = '\\';

	/**
	 * The character serving as default string delimiter character in script
	 * files
	 */
	public static final char defaultStringDelimiter = '"';

	/**
	 * The internal commands available and the object to process the command.
	 */
	public static final String[][] internalCommandsArray = {
			{ "echo", "easyaccept.script.EchoProcessor" },
			{ "expect", "easyaccept.script.ExpectProcessor" },
			{ "expectdifferent", "easyaccept.script.ExpectDifferentProcessor" },
			{ "expecterror", "easyaccept.script.ExpectErrorProcessor" },
			{ "expectwithin", "easyaccept.script.ExpectWithinProcessor" },
			{ "equalfiles", "easyaccept.script.EqualFilesProcessor" },
			{ "quit", "easyaccept.script.QuitProcessor" },
			{ "stringdelimiter", "easyaccept.script.StringDelimiterProcessor" },
			{ "stacktrace", "easyaccept.script.StackTraceProcessor" },
			{ "executescript", "easyaccept.script.ExecuteScriptProcessor" },
			{ "threadpool", "easyaccept.script.ThreadPoolProcessor" },
			{ "repeat", "easyaccept.script.RepeatProcessor" },
			{ "print", "easyaccept.script.PrintProcessor" },
			};

}