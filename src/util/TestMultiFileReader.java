/*
 * Created on 18/12/2003
 *
 */
package util;
import java.io.FileNotFoundException;
import java.io.IOException;

import junit.framework.TestCase;

/*
 * Created on 25/11/2003
 * 
 * To change the template for this generated file go to Window - Preferences -
 * Java - Code Generation - Code and Comments
 */

/**
 * Provide the multi file reader test
 * @author Jacques
 * 
 * To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Generation - Code and Comments
 */
public class TestMultiFileReader extends TestCase {
	/**
	 * Execute the simple read test. 
	 * @throws IOException
	 */
	public void testSimpleRead() throws IOException {
		MultiFileReader mfr = new MultiFileReader();
		mfr.addFile("src/util/test/file1.txt");
		String line1 = mfr.readLine();
		assertEquals("line 1 of file", line1);
		assertEquals("src/util/test/file1.txt", mfr.getCurrentFileName());
		String line2 = mfr.readLine();
		assertNull(line2);
	}
	/**
	 * Execute the Empty File test
	 * @throws IOException
	 */
	public void testEmptyFile() throws IOException {
		MultiFileReader mfr = new MultiFileReader();
		mfr.addFile("src/util/test/emptyFile.txt");
		String line1 = mfr.readLine();
		assertNull(line1);

	}
	/**
	 * Execute the two files test.
	 * @throws IOException
	 */
	public void testTwoFiles() throws IOException {
		MultiFileReader mfr = new MultiFileReader();
		mfr.addFile("src/util/test/file1.txt");
		mfr.addFile("src/util/test/file2.txt");
		String line1 = mfr.readLine();
		assertEquals("line 1 of file", line1);
		assertEquals("src/util/test/file1.txt", mfr.getCurrentFileName());
		String line2 = mfr.readLine();
		assertEquals("src/util/test/file2.txt", mfr.getCurrentFileName());
		assertEquals("line 1 of file2", line2);
		String line3 = mfr.readLine();
		assertNull(line3);
	}
	/**
	 * Execute the empty line reader test.
	 * @throws IOException
	 */
	public void testEmptyLineRead() throws IOException {
		MultiFileReader mfr = new MultiFileReader();
		mfr.addFile("src/util/test/file3.txt");
		String line1 = mfr.readLine();
		assertEquals("line 1", line1);
		String line2 = mfr.readLine();
		assertEquals("", line2);
		String line3 = mfr.readLine();
		assertEquals("line 3", line3);
		String line4 = mfr.readLine();
		assertNull(line4);
	}
	/**
	 * Execute the empty in the middle test
	 * @throws IOException
	 */
	public void testEmptyInTheMiddle() throws IOException {
		MultiFileReader mfr = new MultiFileReader();
		mfr.addFile("src/util/test/file1.txt");
		mfr.addFile("src/util/test/emptyFile.txt");
		mfr.addFile("src/util/test/emptyFile.txt");
		mfr.addFile("src/util/test/file2.txt");
		String line1 = mfr.readLine();
		assertEquals("line 1 of file", line1);
		String line2 = mfr.readLine();
		assertEquals("line 1 of file2", line2);
		String line3 = mfr.readLine();
		assertNull(line3);
	}
	/**
	 * Execute the non existence file test
	 */
	public void testNonExistentFile() {
		MultiFileReader mfr = new MultiFileReader();
		try {
			mfr.addFile("garbage");
			fail("file missing exception should be thrown");
		} catch (FileNotFoundException ex) {
		}
	}
	/**
	 * Execute the repeated read test.
	 * @throws IOException
	 */
	public void testRepeatedRead() throws IOException {
		MultiFileReader mfr = new MultiFileReader();
		mfr.addFile("src/util/test/file1.txt");
		mfr.addFile("src/util/test/file1.txt");
		String line1 = mfr.readLine();
		assertEquals("line 1 of file", line1);
		String line2 = mfr.readLine();
		assertEquals("line 1 of file", line2);
		String line3 = mfr.readLine();
		assertNull(line3);
	}
	/**
	 * Execute the read with close test
	 * @throws IOException
	 */
	public void testReadWithClose() throws IOException {
		MultiFileReader mfr = new MultiFileReader();
		mfr.addFile("src/util/test/file1.txt");
		mfr.addFile("src/util/test/file3.txt");
		String line1 = mfr.readLine();
		assertEquals("line 1 of file", line1);
		String line2 = mfr.readLine();
		assertEquals("line 1", line2);
		String line3 = mfr.readLine();
		assertEquals("", line3);
		assertEquals("src/util/test/file3.txt", mfr.getCurrentFileName());
		mfr.close();
		String line4 = mfr.readLine();
		assertEquals("line 1 of file", line4);
		assertEquals("src/util/test/file1.txt", mfr.getCurrentFileName());
	}
	/**
	 * Execute the char read test
	 * @throws IOException
	 */
	public void testCharRead() throws IOException {
		MultiFileReader mfr = new MultiFileReader();
		mfr.addFile("src/util/test/smallFile.txt");
		assertEquals('l', mfr.read());
		assertEquals('i', mfr.read());
		assertEquals('n', mfr.read());
		assertEquals('e', mfr.read());
		assertEquals(' ', mfr.read());
		assertEquals('1', mfr.read());
		assertEquals('\n', mfr.read());
		assertEquals('z', mfr.read());
		assertEquals('\n', mfr.read());
	}
	/**
	 * Execute the get line number test
	 * @throws IOException
	 */
	public void testGetLineNumber() throws IOException {
		MultiFileReader mfr = new MultiFileReader();
		mfr.addFile("src/util/test/file1.txt");
		mfr.addFile("src/util/test/file3.txt");
		assertEquals(0, mfr.getLineNumber());
		String line1 = mfr.readLine();
		assertEquals("line 1 of file", line1);
		assertEquals(1, mfr.getLineNumber());
		String line2 = mfr.readLine();
		assertEquals("line 1", line2);
		assertEquals(1, mfr.getLineNumber());
		String line3 = mfr.readLine();
		assertEquals("", line3);
		assertEquals(2, mfr.getLineNumber());
		assertEquals("src/util/test/file3.txt", mfr.getCurrentFileName());
		mfr.close();
		assertEquals(0, mfr.getLineNumber());
		String line4 = mfr.readLine();
		assertEquals("line 1 of file", line4);
		assertEquals(1, mfr.getLineNumber());
		assertEquals("src/util/test/file1.txt", mfr.getCurrentFileName());
	}
	/**
	 * Execute the multi file observer test
	 * @throws IOException
	 */
	public void testAddMultiFileReaderObserver() throws IOException {
		MultiFileReader mfr = new MultiFileReader();
		MFRObserver mfo = new MFRObserver();
		mfr.addFile("src/util/test/file1.txt");
		mfr.addFile("src/util/test/file1.txt");
		mfr.addMultiFileReaderObserver(mfo);
		String line1 = mfr.readLine();
		assertEquals("line 1 of file", line1);
		String line2 = mfr.readLine();
		assertEquals("line 1 of file", line2);
		String line3 = mfr.readLine();
		assertNull(line3);
		assertEquals(2, mfo.getNumCalls()); 
	}
	/**(
	 * Execute the Remove Multi File Reader Observer test 
	 * @throws IOException
	 */
	public void testRemoveMultiFileReaderObserver() throws IOException {
		MultiFileReader mfr = new MultiFileReader();
		MFRObserver mfo = new MFRObserver();
		mfr.addFile("src/util/test/file1.txt");
		mfr.addFile("src/util/test/file1.txt");
		mfr.addMultiFileReaderObserver(mfo);
		String line1 = mfr.readLine();
		assertEquals("line 1 of file", line1);
		String line2 = mfr.readLine();
		assertEquals("line 1 of file", line2);
		
		assertEquals(1, mfo.getNumCalls()); 
		mfr.removeMultiFileReaderObserver(mfo);
				
		String line3 = mfr.readLine();
		assertNull(line3);
		assertEquals(1, mfo.getNumCalls()); 
	}
}

/**
 * Implements the MultiFileReaderObserver
 * @author Win Xp
 *
 */
class MFRObserver implements MultiFileReaderObserver {
 
	private int numcalls; 
	/**
	 * Increase the call numbers
	 */
    public void aFileWasClosed(MultiFileEvent event) {
	    numcalls++;
    }
    /**
     * Obtain the call number value
     * @return
     * 			The call number
     */
    public int getNumCalls(){
    	return numcalls;
    }
}