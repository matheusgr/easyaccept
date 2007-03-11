/*
 * Created on 07/06/2004
 * 
 * To change the template for this generated file go to Window - Preferences -
 * Java - Code Generation - Code and Comments
 */
package util;

import java.io.IOException;

/**
 * Provide a logical line reader.
 * @author  Jacques  To change the template for this generated type comment go to Window -  Preferences - Java - Code Generation - Code and Comments
 */
public class LogicalLineReader extends AdapterStream {
	
	public final char NUL = '\u0000';
	private char commentCharacter;
	private char continuation;
	StringBuffer lineBuffer;
	/**
	 * The LogicalLineReader constructor.
	 * @param stream
	 * 			The stream to be used.
	 * @param commentCharacter
	 * 			The commentCharacter char to be used.
	 * @param continuation
	 * 			The continuatiorn char to be used
	 */
	public LogicalLineReader(Stream stream, char commentCharacter,
			char continuation) {
		super(stream);
		this.commentCharacter = commentCharacter;
		this.continuation = continuation;
		lineBuffer = new StringBuffer();
	}
	/**
	 * This method read a line.
	 */
	public String readLine() throws IOException {
		if (!fillBufferWithANonEmptyLine()) {
			return null;
		}
		String line = lineBuffer.toString().trim();
		emptyBuffer();
		return line;
	}

	/**
	 *  Empty the buffer.
 	 */
	private void emptyBuffer() {
		lineBuffer.delete(0, lineBuffer.length());
	}
	/**
	 * Read a char from the buffer and return the number.
	 */
	public int read() throws IOException {
		if (!fillBufferWithANonEmptyLine()) {
			return -1;
		} else {
			int c = lineBuffer.charAt(0);
			lineBuffer.deleteCharAt(0);
			return c;
		}
	}
	/**
	 * Set as unread.
	 */
	public void unread(int nextChar) throws IOException {
		lineBuffer.insert(0, (char) nextChar);
	}
	/**
	 * Fill the buffer with lines.
	 * @return
	 * 			"yes" if it was possible to fill the buffer. Otherwise "no".
	 * @throws IOException
	 */
	private boolean fillBufferWithANonEmptyLine() throws IOException {
		if (lineBuffer.length() > 0) {
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
			return true;
		}
	}
	/**
	 * Fill the buffer with a logical line.
	 * @return
	 * 			"yes" if it was possible to fill the buffer. Otherwise "no".
	 * @throws IOException
	 */
	private boolean fillBufferWithALogicalLine() throws IOException {
		// must process continuation characters
		boolean readMore = true;
		while (readMore) {
			int c = stream.read();
			if (c < 0) {
				// end of file was found.
				readMore = false;
			} else if (continuation != NUL && c == continuation) {
				c = stream.read();
				if (c < 0) {
					readMore = false;
				}
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
		return lineBuffer.length() > 0;
	}
	
	/**
	 * @return  Returns the continuation.
	 * @uml.property  name="continuation"
	 */
	public char getContinuation(){
		return this.continuation;
	}

}