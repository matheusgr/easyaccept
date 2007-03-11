/*
 * Created on 07/06/2004
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package util;

import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * The Stream interface determine your type behavior .
 * @author Jacques
 *
 * To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public interface Stream {
	public static final int END_OF_FILE = -1; 
	public void addFile(String fileName) throws FileNotFoundException;
	public String readLine() throws IOException;
	public void close();
	public int read() throws IOException;
	public void unread(int nextChar) throws IOException;
	public String getCurrentFileName();
	public int getLineNumber();
}
