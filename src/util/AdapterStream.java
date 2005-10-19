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
 * @author Jacques
 *
 * To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public class AdapterStream implements Stream {

	protected Stream stream;

	public AdapterStream(Stream stream) {
		this.stream = stream;
	}

	public void addFile(String fileName) throws FileNotFoundException {
		stream.addFile(fileName);
	}

	public void close() {
		stream.close();
	}
	public String readLine() throws IOException {
		return stream.readLine();
	}
	public int read() throws IOException {
		return stream.read();
	}

	public void unread(int nextChar) throws IOException {
		stream.unread(nextChar);
	}

	/* (non-Javadoc)
	 * @see util.Stream#getCurrentFileName()
	 */
	public String getCurrentFileName() {
		return stream.getCurrentFileName();
	}
	/**
	 * @return
	 */
	public int getLineNumber() {
		return stream.getLineNumber();
	}

}
