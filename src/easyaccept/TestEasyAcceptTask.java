package easyaccept;

import java.io.File;

import org.apache.tools.ant.BuildFileTest;


/**
 * Tests <code>EasyAcceptTask</code>. 
 * @author Guilherme Mauro Germoglio - germoglio@gmail.com
 */
public class TestEasyAcceptTask extends BuildFileTest {
	/**
	 * TestEasyAcceptTesk constructor.
	 * @param name
	 */
	public TestEasyAcceptTask(String name) {
		super(name);
	}
	
	/**
	 * The TestEasyAcceptTesk set up.
	 */
	public void setUp() throws Exception {
		configureProject("tests" + File.separator + "test_task_build.xml");
	}
	
	/**
	 * Test a simple execution1.
	 */
	public void testSimpleExecution1() {
		executeTarget("simple.execution.1");
	}
	/**
	 * Test a simple execution2.
	 */
	public void testSimpleExecution2() {
		executeTarget("simple.execution.2");
	}
	/**
	 * Test a simple execution3.
	 */
	public void testSimpleExecution3() {
		executeTarget("simple.execution.3");
	}
	
	/**
	 * Test no file1 existence.
	 */
	public void testNoFiles1() {
		expectLog("no.files.1", "No files found.");
	}
	/**
	 * Test no file2 existence.
	 */
	public void testNoFiles2() {
		expectLog("no.files.2", "No files found.");
	}
	/**
	 * Test no file3 existence.
	 */	
	public void testNoFiles3() {
		expectBuildException("no.files.3", "No files found.");
	}
	/**
	 * Test no file4 existence.
	 */
	public void testNoFiles4() {
		expectBuildException("no.files.4", "No files found.");
	}
	/**
	 * Test no facade1 existence.
	 */
	public void testNoFacade1() {
		executeTarget("no.facade.1");
	}
	/**
	 * Test no facade2 existence.
	 */
	public void testNoFacade2() {
		executeTarget("no.facade.2");
	}
	/**
	 * Test no facade3 existence.
	 */
	public void testNoFacade3() {
		expectBuildException("no.facade.3", "No Facade specified.");
	}
	/**
	 * Test no facade4 existence.
	 */
	public void testNoFacade4() {
		expectBuildException("no.facade.4", "The Facade class was not found.");
	}
	/**
	 * Test the property1 failure.
	 */
	public void testFailureProperty1() {
		expectBuildException("failure.property.1", "Failure expected.");
	}
}
