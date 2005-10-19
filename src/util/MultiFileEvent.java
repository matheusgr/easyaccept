/*
 * Created on Dec 6, 2004
 */
package util;

import java.util.EventObject;

/**
 * @author roberta
 */
public class MultiFileEvent  extends EventObject {

	public MultiFileEvent(MultiFileReader source) {
		super(source);
	}

}
