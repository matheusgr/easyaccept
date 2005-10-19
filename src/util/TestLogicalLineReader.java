/*
 * Created on 07/06/2004
 * 
 * To change the template for this generated file go to Window - Preferences -
 * Java - Code Generation - Code and Comments
 */
package util;

import java.io.IOException;

import junit.framework.TestCase;

/**
 * @author Jacques
 * 
 * To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Generation - Code and Comments
 */
public class TestLogicalLineReader extends TestCase {
	public void testSimpleFile() throws IOException {
		Stream llr = new LogicalLineReader(new MultiFileReader(), '#', '\\');
		llr.addFile("src/util/test/file1.txt");
		String line1 = llr.readLine();
		assertEquals("line 1 of file", line1);
		String line2 = llr.readLine();
		assertNull(line2);
		llr.close();
	}
	public void testComment() throws IOException {
		Stream llr = new LogicalLineReader(new MultiFileReader(), '#', '\\');
		llr.addFile("src/util/test/fileWithComments.txt");
		assertEquals("a line", llr.readLine());
		assertNull(llr.readLine());
		llr.close();
	}
	public void testRead() throws IOException {
		Stream llr = new LogicalLineReader(new MultiFileReader(), '#', '\\');
		llr.addFile("src/util/test/smallFileWithComments.txt");
		assertEquals('u', llr.read());
		assertEquals('\n', llr.read());
		assertEquals(Stream.END_OF_FILE, llr.read());
	}
	
	public void testLineWithSpaces() throws IOException {
		Stream llr = new LogicalLineReader(new MultiFileReader(), '#', '\\');
		llr.addFile("src/util/test/fileWithSpaces.txt");
		assertEquals("a line", llr.readLine());
		assertEquals("a second line", llr.readLine());
		assertNull(llr.readLine());
		llr.close();
	}
	public void testLineWithSpacesReadByCharacter() throws IOException {
		Stream llr = new LogicalLineReader(new MultiFileReader(), '#', '\\');
		llr.addFile("src/util/test/fileWithSpaces.txt");
		assertEquals('a', llr.read());
		assertEquals(' ', llr.read());
		assertEquals('l', llr.read());
		assertEquals('i', llr.read());
		assertEquals('n', llr.read());
		assertEquals('e', llr.read());
		assertEquals('\n', llr.read());
		assertEquals('a', llr.read());
		assertEquals(' ', llr.read());
		assertEquals('s', llr.read());
		assertEquals('e', llr.read());
		assertEquals('c', llr.read());
		assertEquals('o', llr.read());
		assertEquals('n', llr.read());
		assertEquals('d', llr.read());
		assertEquals(' ', llr.read());
		assertEquals('l', llr.read());
		assertEquals('i', llr.read());
		assertEquals('n', llr.read());
		assertEquals('e', llr.read());
		assertEquals('\n', llr.read());
		assertEquals(Stream.END_OF_FILE, llr.read());
		llr.close();
	}
	public void testMixedRead() throws IOException {
		Stream llr = new LogicalLineReader(new MultiFileReader(), '#', '\\');
		llr.addFile("src/util/test/fileWithSpaces.txt");
		assertEquals('a', llr.read());
		assertEquals(' ', llr.read());
		assertEquals('l', llr.read());
		assertEquals("ine", llr.readLine());
		assertEquals("a second line", llr.readLine());
		assertNull(llr.readLine());
		llr.close();
	}
	public void testUnread() throws IOException {
		Stream llr = new LogicalLineReader(new MultiFileReader(), '#', '\\');
		llr.addFile("src/util/test/fileWithSpaces.txt");
		assertEquals('a', llr.read());
		llr.unread('c');
		assertEquals('c', llr.read());
		assertEquals(' ', llr.read());
		assertEquals('l', llr.read());
		assertEquals("ine", llr.readLine());
		assertEquals("a second line", llr.readLine());
		assertNull(llr.readLine());
		llr.close();
	}
	
	public void testBackSlashRemains() throws IOException {
		Stream llr = new LogicalLineReader(new MultiFileReader(), '#', '\\');
		llr.addFile("src/util/test/backSlashTest.txt");
	    assertEquals("param=\\\"", llr.readLine());
	}

}
