package easyaccept;

import java.io.File;

import org.apache.tools.ant.BuildFileTest;


/**
 * Tests <code>EasyAcceptTask</code>. 
 * @author Guilherme Mauro Germoglio - germoglio@gmail.com
 */
public class TestEasyAcceptTask extends BuildFileTest {
	
	public TestEasyAcceptTask(String name) {
		super(name);
	}

	public void setUp() throws Exception {
		configureProject("tests" + File.separator + "test_task_build.xml");
	}
	
	public void testSimpleExecution1() {
		executeTarget("simple.execution.1");
	}
	
	public void testSimpleExecution2() {
		executeTarget("simple.execution.2");
	}
	
	public void testSimpleExecution3() {
		executeTarget("simple.execution.3");
	}
	
	public void testNoFiles1() {
		expectLog("no.files.1", "No files found.");
	}
	
	public void testNoFiles2() {
		expectLog("no.files.2", "No files found.");
	}
		
	public void testNoFiles3() {
		expectBuildException("no.files.3", "No files found.");
	}
	
	public void testNoFiles4() {
		expectBuildException("no.files.4", "No files found.");
	}
	
	public void testNoFacade1() {
		executeTarget("no.facade.1");
	}
	
	public void testNoFacade2() {
		executeTarget("no.facade.2");
	}
	
	public void testNoFacade3() {
		expectBuildException("no.facade.3", "No Facade specified.");
	}
	
	public void testNoFacade4() {
		expectBuildException("no.facade.4", "The Facade class was not found.");
	}
	
	public void testFailureProperty1() {
		expectBuildException("failure.property.1", "Failure expected.");
	}
}
