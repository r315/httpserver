package local;

import java.util.List;

public interface ResourseSupplier {
	
	public List<String> readData();
	void writeData(String data);

}
