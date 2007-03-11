/*
 * Created on Oct 28, 2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package easyaccept.script;


import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.io.Reader;

import util.Parameter;
import util.ParsedLine;
import easyaccept.EasyAcceptException;


/**
 * Provide the aqual files command.
 * @author roberta
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class EqualFilesProcessor  implements Command{

	/**
	 * Execute the equal files command.
	 */
	public Object execute(Script script, ParsedLine parsedLine) throws Exception {
		
    	if (parsedLine.numberOfParameters() != 3) {
            throw new EasyAcceptException(script.getFileName(), script
                    .getLineNumber(),
                    "Syntax error: equalfiles <fileOK> <fileToTest>");
        }
    	Parameter [] param = parsedLine.getCommandArgs(); 
    	try{
    		equalFiles(param[0].getValueAsString(),
    				param[1].getValueAsString());
    	}catch(EasyAcceptException exc){
    		throw new EasyAcceptException(script.getFileName(), script
                    .getLineNumber(), exc.getMessage());    		
    	}	
        return "OK";  
	}
	/**
	 * Make the equal files test. 
	 * @param fileOK
	 * 				A file to be compared.
	 * @param fileToTest
	 * 				A file to be testes as equal the other one.
	 * @throws IOException
	 * @throws EasyAcceptException
	 */
    private static void equalFiles(String fileOK, String fileToTest) throws IOException, EasyAcceptException { 
       		
    	LineNumberReader lineReaderOK = null;
       	LineNumberReader lineReaderToTest = null;
       	try {
       	    Reader readerOK = new FileReader( new File(fileOK) );
       	    lineReaderOK = new LineNumberReader( readerOK );
       	      
       	    Reader readerToTest = new FileReader( new File(fileToTest) );
       	    lineReaderToTest = new LineNumberReader( readerToTest );
       	      
       		String lineOK = lineReaderOK.readLine();
       		String lineToTest = lineReaderToTest.readLine();
     
       		while( (lineOK != null) && (lineToTest!= null) ){ 
       			if ( ! (lineOK.trim().equals(lineToTest.trim())) ) {
       			
       				throw new EasyAcceptException( "File diferences at line "+ lineReaderOK.getLineNumber()+". Expected <"
                            + lineOK.trim()+ ">, but was "
                            +  "<"+lineToTest.trim()+">");   
       			}
       			lineOK = lineReaderOK.readLine();
       	        lineToTest = lineReaderToTest.readLine();
       	    }
       		if (lineOK == null && lineToTest != null) {
       			throw new EasyAcceptException( "File diferences at line "+ lineReaderOK.getLineNumber()+". Expected the end of file was but"
       					+" extra line was found "+"< "+lineToTest.trim()+">");
           	}else if (lineOK != null && lineToTest == null) {
       			throw new EasyAcceptException( "File diferences at line "+ lineReaderOK.getLineNumber()+". Expected <"+
                          lineOK.trim()+">, but the end of file was found. ");
           	}else if (lineOK == null && lineToTest == null) {
   		    }
       	} finally{
       			if(lineReaderOK!= null){
       				lineReaderOK.close();
       			}
       			if(lineReaderToTest != null){
       				lineReaderToTest.close();
       			}
       	}
   	}    
}
