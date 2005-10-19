/*
 * Project: topogiggio
 * Script.java 
 *
 * Copyright 2004 Universidade federal da Campina Grande. All rights reserved.
 */

package easyaccept;

/**
 * The <code>EasyAcceptSyntax</code> class ...
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
			{ "expecterror", "easyaccept.script.ExpectErrorProcessor" },
			{ "equalfiles", "easyaccept.script.EqualFilesProcessor" },
			{ "quit", "easyaccept.script.QuitProcessor" },
			{ "stringdelimiter", "easyaccept.script.StringDelimiterProcessor" },
			{ "stacktrace", "easyaccept.script.StackTraceProcessor" }, };

}