/*
 * Created on 07/06/2004
 * 
 * To change the template for this generated file go to Window - Preferences -
 * Java - Code Generation - Code and Comments
 */
package util;

import java.io.IOException;

/**
 * @author Jacques
 * 
 * To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Generation - Code and Comments
 */



public class LogicalLineReader extends AdapterStream {
	public final char NUL = '\u0000';

	private char commentCharacter;

	private char continuation;

	StringBuffer lineBuffer;

	public LogicalLineReader(Stream stream, char commentCharacter,
			char continuation) {
		super(stream);
		this.commentCharacter = commentCharacter;
		this.continuation = continuation;
		lineBuffer = new StringBuffer();
	}

	public String readLine() throws IOException {
		if (!fillBufferWithANonEmptyLine()) {
			return null;
		}
		//		System.err.println("returning line: " + lineBuffer.length());
		String line = lineBuffer.toString().trim();
		emptyBuffer();
		return line;
	}

	/**
	 *  
	 */
	private void emptyBuffer() {
		lineBuffer.delete(0, lineBuffer.length());
	}

	public int read() throws IOException {
		if (!fillBufferWithANonEmptyLine()) {
			return -1;
		} else {
			int c = lineBuffer.charAt(0);
			lineBuffer.deleteCharAt(0);
//			System.out.println("logic line reader returns " + (char)c);
			return c;
		}
	}

	public void unread(int nextChar) throws IOException {
		//		System.err.println("unread " + (char) nextChar + "(" + nextChar +
		// ")");
		lineBuffer.insert(0, (char) nextChar);
	}

	private boolean fillBufferWithANonEmptyLine() throws IOException {
		if (lineBuffer.length() > 0) {
			// we already have a line
			return true;
		}
		while (true) {
			if (!fillBufferWithALogicalLine()) {
				return false;
			}
			if (lineBuffer.toString().trim().length() == 0) {
				emptyBuffer();
				continue;
			}
			if (lineBuffer.length() > 0
					&& lineBuffer.charAt(0) == commentCharacter) {
				emptyBuffer();
				continue;
			}
			//			System.err.println("filled: length: "
			//					+ lineBuffer.toString().trim().length());
			return true;
		}
	}

	private boolean fillBufferWithALogicalLine() throws IOException {
		// must process continuation characters
		boolean readMore = true;
		while (readMore) {
			int c = stream.read();
			//System.err.println("reading char:: " + (char) c + "  int: "+ c);
			//System.err.println("Stream.EOF" + Stream.END_OF_FILE);
			if (c < 0) {
				// end of file was found.
				readMore = false;
			} else if (continuation != NUL && c == continuation) {
				c = stream.read();
				if (c < 0) {
					readMore = false;
				}
				
				//to allow the following syntax: stringDelimiter=\"
				if (! (c == '\n') ) {
					lineBuffer.append(continuation);
					lineBuffer.append((char) c);
				}
			} else {
				if (c == '\n') {
					readMore = false;
				}
				lineBuffer.append((char) c);
			}
		}
		// have we returned something?
//		System.err.println("Return:: <" + lineBuffer.toString() + "> tam:: " + lineBuffer.length());
		return lineBuffer.length() > 0;
	}
	
	public char getContinuation(){
		return this.continuation;
	}

}


// TODO (todolist) LogicalLineReaderHasABuffer to allow unread, and store in a buffer the lines from MultiFileReader