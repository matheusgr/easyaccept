/*
 * Created on Dec 6, 2004
 */
package util;

import java.util.EventObject;

/**
 * The MultiFileEvent class define an event that will be used by multi file. 
 * @author roberta
 */
public class MultiFileEvent  extends EventObject {

	/**
	 * Construct a MultiFileEvent object.
	 * @param source
	 * 			The MultiFileReader object to obtain the event.
	 */
	public MultiFileEvent(MultiFileReader source) {
		super(source);
	}

}
