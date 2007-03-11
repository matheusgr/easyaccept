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
 * The MultiFileReader class make possible work out and read multi file.
 * @author  Jacques  To change the template for this generated type comment go to Window -  Preferences - Java - Code Generation - Code and Comments
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

	/**
	 * Construct a MultiFileReader object.
	 */
	public MultiFileReader() {
		fileList = new ArrayList();
		startAtFirstFile();
	}

	/**
	 * Determine the first file informations.
	 */
	private void startAtFirstFile() {
		nextFile = 0;
		currentFileName = null;
		lineNumber = 0;
	}

	/**
	 * Add a new file to the MultiFileReader object.
	 * @param string
	 * 			The file name to be added. 
	 */
	public void addFile(String fileName) throws FileNotFoundException {
		File file = new File(fileName);
		if (file.isDirectory()) {
			if (!fileName.endsWith("/CVS")) {
				String[] list = file.list();
				for (int i = 0; i < list.length; i++) {
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
	 * Read line by the current file.
	 * @return
	 * 			The read line.
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
	 * Read a line by the current file.
	 * @return
	 * 			The read line.
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
	 *  Close the current file.
	 */
	private void closeFile() {
		if (reader != null) {
			try {
				reader.close();
				notifyObservers(); 
			} catch (IOException e) {
			}
			reader = null;
		}
	}
	/**
	 *  Open the next file.
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
	
	/**
	 * Inform it has next file.
	 * @return
	 * 			yes if has next file or false otherwise.
	 */
	private boolean hasNextFile() {
		return nextFile < fileList.size();
	}

	/**
	 * Obtain the next file.
	 * @return
	 * 			The next file.
	 */
	private String nextFile() {
		return (String) fileList.get(nextFile++);
	}

	/**
	 *  Close a file.
	 */
	public void close() {
		closeFile();
		startAtFirstFile();
	}
	/**
	 * Set the previous character.
	 * @param prevchar
	 * 			The new previous character.
	 */
	private void setPrevCharacter( int prevchar ){
		this.previousCharacter = prevchar;
	}
	
	/**
	 * Return the previous character.
	 * @return
	 * 			The previous character.
	 */
	private int getPrevCharacter(){
		return previousCharacter;
	}
	
	/**
	 * Read the file and return a character referring to the file reader.
	 * @return
	 * 			The read character. 
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

	/**
	 * Determine if the character represents an unread line.
	 */
	public void unread(int nextChar) throws IOException {
		reader.unread(nextChar);
		if(nextChar == '\n') {
			lineNumber--;
		}
	}

	/**
	 * Ontain the current file name.
	 * @return  Returns 
	 * 			The currentFileName.
	 * @uml.property  name="currentFileName"
	 */
	public String getCurrentFileName() {
		return currentFileName;
	}

	/**
	 * Return the line number.
	 * @return  
	 * 			Returns the lineNumber.
	 * @uml.property  name="lineNumber"
	 */
	public int getLineNumber() {
		return lineNumber;
	}
	
	/**
	 * Add an MultiFileReader observer at the multiFileReaderObservers collection.
	 * @param mfrl
	 * 			The MultiFileReaderObserver to be added.
	 */
	public synchronized void addMultiFileReaderObserver(MultiFileReaderObserver mfrl) {
		if(!multiFileReaderObservers.contains(mfrl)) {
			multiFileReaderObservers.add(mfrl);
        }
	}
	
	/**
	 * Remove an MultiFileReader observer at the multiFileReaderObservers collection.
	 * @param mfrl
	 * 			The MultiFileReaderObserver to be removed.
	 */
	public synchronized void removeMultiFileReaderObserver(MultiFileReaderObserver mfrl) {
		multiFileReaderObservers.remove( mfrl);
	}

	/**
	 * Notify the observers if a file was closed.
	 */
	public void notifyObservers(){
		Collection listeners;
	        synchronized (this) {
	            //to avoid synchronization problems
	        	listeners = (Collection)(((Vector)multiFileReaderObservers).clone());
	        }
	        MultiFileEvent event = new MultiFileEvent(this);
	        Iterator it = listeners.iterator();
	        while(it.hasNext()) {
	            ((MultiFileReaderObserver)(it.next())).aFileWasClosed(event);
	        }
	}
	
}
