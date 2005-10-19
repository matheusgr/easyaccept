/*
 * Created on 18/12/2003
 * 
 * To change the template for this generated file go to Window - Preferences -
 * Java - Code Generation - Code and Comments
 */
package util;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PushbackReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

/*
 * Created on 25/11/2003
 * 
 * To change the template for this generated file go to Window - Preferences -
 * Java - Code Generation - Code and Comments
 */

/**
 * @author Jacques
 * 
 * To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Generation - Code and Comments
 */
public class MultiFileReader implements Stream {
	private List fileList;
	private int nextFile;
	private PushbackReader reader = null;
	private final int SIZE_PUSHBACK = 10;
	private String currentFileName;
	private int lineNumber;
	private int previousCharacter;
	private Collection multiFileReaderObservers = new Vector();

	public MultiFileReader() {
		fileList = new ArrayList();
		startAtFirstFile();
	}

	private void startAtFirstFile() {
		nextFile = 0;
		currentFileName = null;
		lineNumber = 0;
	}

	/**
	 * @param string
	 */
	public void addFile(String fileName) throws FileNotFoundException {
		File file = new File(fileName);
		if (file.isDirectory()) {
			// to help me test, don't go down CVS trees
			if (!fileName.endsWith("/CVS")) {
				String[] list = file.list();
				for (int i = 0; i < list.length; i++) {
					//System.err.println("Inserting File: "+fileName + "/" + list[i] );
					addFile(fileName + "/" + list[i]);
				}
			}
		} else {
			if (!file.canRead()) {
				throw new FileNotFoundException();
			}
			fileList.add(fileName);
		}
	}

	/**
	 * @return
	 */
	public String readLine() throws IOException {
		String line = null;
		while (line == null && openFile()) {
			line = readALine();
			if (line == null) {
				closeFile();
			}
		}
		return StringUtil.removeNewLine(line);
	}

	/**
	 * @return
	 */
	private String readALine() throws IOException {
		StringBuffer sb = null;
		int c;
		while ((c = reader.read()) >= 0) {
			if (sb == null) {
				sb = new StringBuffer();
			}
			if (c != '\r') {
				sb.append((char) c);
			}
			if (c == '\n') {
				lineNumber++;
				break;
			}
		}
		return sb == null ? null : sb.toString();
	}

	/**
	 *  
	 */
	private void closeFile() {
		if (reader != null) {
			try {
				reader.close();
				notifyObservers(); 
			} catch (IOException e) {
				// since we're reading and not writing, there shouldn't be exceptions while closing
				// ignore
			}
			reader = null;
		}
	}
	/**
	 *  
	 */
	private boolean openFile() throws FileNotFoundException {
		if (reader == null && hasNextFile()) {
			currentFileName = nextFile();
			reader =
				new PushbackReader(
					new BufferedReader(new FileReader(currentFileName)), SIZE_PUSHBACK);
			lineNumber = 0;
		}
		return reader != null;
	}

	private boolean hasNextFile() {
		return nextFile < fileList.size();
	}

	private String nextFile() {
		return (String) fileList.get(nextFile++);
	}

	/**
	 *  
	 */
	public void close() {
		closeFile();
		startAtFirstFile();
	}

	private void setPrevCharacter( int prevchar ){
		this.previousCharacter = prevchar;
	}
	
	private int getPrevCharacter(){
		return previousCharacter;
	}
	
	/**
	 * @return
	 */
	public int read() throws IOException {
		boolean readMore = true;
		int carac = END_OF_FILE;
		while (readMore && openFile()) {
			carac = reader.read();
			
			if (carac == '\r') {
				continue;
			}
			
			if (carac == '\n') {
				lineNumber++;
			}
		
			if (carac == END_OF_FILE) {
				
				// If the END_OF_FILE was reached and there was not a new line feed (\n)
				// We needed to include a new line feed on the stream to avoid MultiFileReader 
				// to merge lines of different files.

				if (getPrevCharacter() != '\n') {
					setPrevCharacter(carac);
					carac = '\n';
					//System.err.println("End of file without new line!");
				}else{
					setPrevCharacter(carac);
				}
				closeFile();
				
			}else{
				setPrevCharacter(carac);
			}

			readMore = false;
		}

		
		return carac;
	}

	public void unread(int nextChar) throws IOException {
//		System.err.println("reader unread: reader=" + reader + ", char = " + nextChar);
		reader.unread(nextChar);
		if(nextChar == '\n') {
			lineNumber--;
		}
	}

	/**
	 * @return Returns the currentFileName.
	 */
	public String getCurrentFileName() {
		return currentFileName;
	}

	public int getLineNumber() {
		return lineNumber;
	}
	
	public synchronized void addMultiFileReaderObserver(MultiFileReaderObserver mfrl) {
		if(!multiFileReaderObservers.contains(mfrl)) {
			multiFileReaderObservers.add(mfrl);
        }
	}
	
	public synchronized void removeMultiFileReaderObserver(MultiFileReaderObserver mfrl) {
		multiFileReaderObservers.remove( mfrl);
	}

	public void notifyObservers(){
		Collection listeners;
	        synchronized (this) {
	            // to avoid synchronization problems
	        	listeners = (Collection)(((Vector)multiFileReaderObservers).clone());
	        }
	        MultiFileEvent event = new MultiFileEvent(this);
	        Iterator it = listeners.iterator();
	        while(it.hasNext()) {
	            ((MultiFileReaderObserver)(it.next())).aFileWasClosed(event);
	        }
	}

	
	
}
