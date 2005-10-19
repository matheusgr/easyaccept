package util;

/*
 * Created on 25/11/2003
 * 
 * To change the template for this generated file go to Window - Preferences -
 * Java - Code Generation - Code and Comments
 */

/**
 * @author Roberta Coelho
 *  
 * This interface is used to implement the Observer design pattern 
 * The class that implements this interface will be notified when a file
 * in MultifileReader was closed
 */
public interface MultiFileReaderObserver extends java.util.EventListener {
	public void aFileWasClosed(MultiFileEvent event);
}