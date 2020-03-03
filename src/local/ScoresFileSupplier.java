package local;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;

public class ScoresFileSupplier implements ResourseSupplier {
	private Path filePath;
	private static Logger log;
	
	public ScoresFileSupplier(String filename){
		filePath = Paths.get(filename);			//mut be full path
		log = new Logger("File Supplier");
	}
	
	@Override
	public List<String> readData(){
		  try {         	  
	           return Files.readAllLines(filePath);
	        } catch (IOException e){// | URISyntaxException e) {
	           log.log("Error reading File >" + e.toString());
	            return null;
	        }
	}

	@Override
	public void writeData(String data) {
		
		if(data.split(" ").length != 2)
			return;		
		try {	 
            Files.write(filePath, data.getBytes(), StandardOpenOption.APPEND);
        } catch (IOException e){// | URISyntaxException e) {
           log.log("Error Writing to File >" + e.toString());
        }		
	}
	
	public void clear(){
		try {	 
            Files.write(filePath, "".getBytes());
        } catch (IOException e){
           log.log("Error clering scores File >" + e.toString());
        }		
	}

}
