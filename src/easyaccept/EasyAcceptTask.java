package easyaccept;

import java.util.Iterator;
import java.util.Vector;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.types.Path;
import util.Variables;
import util.VariablesImpl;

/**
 * This class represents an Ant task that executes EasyAccept.
 * @author  Guilherme Mauro Germoglio - germoglio@gmail.com
 */
public class EasyAcceptTask extends Task {

	/**
	 * Property value to be set when there's a failure during tests and 'failureproperty' attribute is set. 
	 */
	private static final String TRUE_VALUE = "true";

	/**
	 * Message shown when no facade attribute was specified.
	 */
	private static final String NO_FACADE_SPECIFIED_MSG = "No Facade specified.";
	/**
	 * Message shown when the facade attribute was specified but the class was not found.  
	 */
	private static final String THE_FACADE_CLASS_WAS_NOT_FOUND_MSG = "The Facade class was not found.";
	/**
	 * Message shown when no files are found.
	 */
	private static final String NO_FILES_FOUND_MSG = "No files found.";

	/**
	 * Attribute to make the build fail when there's any error during the
	 * execution. The default value is true. It is not required.
	 */
	private boolean failonerror = true;

	/**
	 * Attribute that holds the facade class to be used. It is required.
	 */
	private String facade;

	/**
	 * The paths to be used by EasyAccept.
	 */
	private Vector<Path> paths = new Vector();

	/**
	 * The property to be set when there's a failure during the tests.
	 */
	private String failureproperty;

	/**
	 * Sets the value of 'failonerror' attribute.
	 * @param failonerror  The attribute value.
	 * @uml.property  name="failonerror"
	 */
	public void setFailonerror(boolean failonerror) {
		this.failonerror = failonerror;
	}

	/**
	 * Sets the value of 'failureproperty' attribute.
	 * @param failureproperty  The name of the property to be declared and set  <code>true</code> when there's an failure during tests.
	 * @uml.property  name="failureproperty"
	 */
	public void setFailureproperty(String failureproperty) {
		this.failureproperty = failureproperty;
	}

	/**
	 * Sets the value of 'facade' attribute.
	 * @param facade  The attribute value.
	 * @uml.property  name="facade"
	 */
	public void setFacade(String facade) {
		this.facade = facade;
	}

	/**
	 * Adds a 'path' element value.
	 * 
	 * @param path
	 *            The 'path' element.
	 */
	public void addPath(Path path) {
		paths.add(path);
	}

	/**
	 * Do the execution.
	 */
	public void execute() {
		
		if (validate()) {
			EasyAccept tester = new EasyAccept();
			Class facadeClass = null;
			Object facadeObj = null;
			try {
				facadeClass = Class.forName(facade);
				facadeObj = facadeClass.newInstance();
			} catch (Exception e) {
				throw new BuildException("Invalid Facade class: " + facade);
			}
			int statusCode = 0;
			statusCode = runForEachPath(statusCode,tester,facadeObj);	
			if (statusCode != 0) {
				if (failureproperty != null) {
					getProject().setNewProperty(failureproperty, TRUE_VALUE);
				}
			}

		}
	}

	/**
	 * Execute for each path
	 * @param statusCode
	 * 			The code expressing the execution correctness. 
	 * @param tester
	 * 			The object that runs the acceptance tests.
	 * @param facadeObj
	 * 			The facade
	 * @return
	 * 			An int expressing the execution correctness.
	 */
	private int runForEachPath(int statusCode, EasyAccept tester, Object facadeObj) {
		
		Iterator pathIterator = paths.iterator();
		Variables variables = new VariablesImpl();
		
		while (pathIterator.hasNext()) {
			Path path = (Path) pathIterator.next();
			String[] files = path.list();
			for (int k = 0; k < files.length; k++) {
				String file = files[k];
				try {
					if (!tester.runAcceptanceTest(facadeObj, file, variables)) {
						statusCode = 1;
					}
				} catch (QuitSignalException e1) {
					getProject().log(e1.getMessage());
					statusCode = 0;
				} catch (Exception e) {
					getProject().log(e.getMessage());
					statusCode = 1;
				}
			}
		}
		return statusCode;
	}

	/**
	 * Validates the task:
	 * 
	 * 1. Checks if the facade was set and is a valid Java class.
	 * 
	 * 2. Checks if the path was set and has any file.
	 * 
	 * @return <code>true</code> when the task is able to execute.
	 *         <code>false</code> otherwise.
	 * @throws BuildException When failonerror is <code>true</code> and the task is invalid.
	 */
	private boolean validate() {
		
		if (facade == null) {
			if (failonerror) {
				throw new BuildException(NO_FACADE_SPECIFIED_MSG);
			} else {
				getProject().log(NO_FACADE_SPECIFIED_MSG);
				return false;
			}
		} else {
			if (!validateFacade(facade)) {
				return false;
			}
		}
		if (paths.size() < 1) {
			if (failonerror) {
				throw new BuildException(NO_FILES_FOUND_MSG);
			} else {
				getProject().log(NO_FILES_FOUND_MSG);
				return false;
			}
		} else {
			if (!validadePaths(paths)) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Validates the paths. Checks if the paths specify any file.
	 * @param paths The Vector of paths.
	 * @return <code>true</code> when there's at least one file specified by the paths. <code>false</code> otherwise.
	 * @throws BuildException when there's no file specified and 'failonerror' attribute is <code>true</code>.
	 */
	private boolean validadePaths(Vector paths) {
		int numberOfFiles = 0;
		
		Iterator pathIt = paths.iterator();
		while (pathIt.hasNext()) {
			Path path = (Path) pathIt.next();
			numberOfFiles += path.list().length;
		}
		if (numberOfFiles == 0) {
			if (failonerror) {
				throw new BuildException(NO_FILES_FOUND_MSG);
			} else {
				getProject().log(NO_FILES_FOUND_MSG);
				return false;
			}
		}
		return true;			
	}

	/**
	 * Validates the facade. Checks whether the class exists or not. Throws a
	 * BuildException if it doesn't exist and the 'failonerror' attribute is
	 * <code>true</code>.
	 * 
	 * @param facadeClass
	 *            The facade class name.
	 * @return <code>true</code> case the class is valid. <code>false</code>
	 *         otherwise.
	 *         Throws a
	 * @throws BuildException if the Facade doesn't exist and the 'failonerror' attribute is
	 * <code>true</code>.
	 */
	private boolean validateFacade(String facadeClass) {
		try {
			Class.forName(facadeClass);
		} catch (ClassNotFoundException e) {
			if (failonerror) {
				throw new BuildException(THE_FACADE_CLASS_WAS_NOT_FOUND_MSG);
			} else {
				getProject().log(THE_FACADE_CLASS_WAS_NOT_FOUND_MSG);
				return false;
			}
		}
		return true;
	}
}
