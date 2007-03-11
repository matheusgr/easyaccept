/*
 * Created on 10/06/2004
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package util;

import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Create an adapter stream.
 * @author  Jacques  To change the template for this generated type comment go to  Window - Preferences - Java - Code Generation - Code and Comments
 */
public class AdapterStream implements Stream {

	protected Stream stream;

	/**
	 * Construct an AdapterStream object.
	 * @param stream
	 * 			The stream to be used.
	 */
	public AdapterStream(Stream stream) {
		this.stream = stream;
	}

	/**
	 * Add a new file at the current stream.
	 */
	public void addFile(String fileName) throws FileNotFoundException {
		stream.addFile(fileName);
	}

	/**
	 *Close the current stream.
	 */
	public void close() {
		stream.close();
	}
	/**
	 * Read a line by th stream.
	 */
	public String readLine() throws IOException {
		return stream.readLine();
	}
	/**
	 * The method read the current stream.
	 */
	public int read() throws IOException {
		return stream.read();
	}
	/**
	 * 
	 */
	public void unread(int nextChar) throws IOException {
		stream.unread(nextChar);
	}
	/**
	 * Obtain the current file name.
	 */
	public String getCurrentFileName() {
		return stream.getCurrentFileName();
	}
	/**
	 * Obtain the line number.
	 * @return
	 * 			The line number.
	 */
	public int getLineNumber() {
		return stream.getLineNumber();
	}

}
