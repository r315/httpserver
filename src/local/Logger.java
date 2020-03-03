package local;

public class Logger {
	private String tag;
	
	
	public Logger(String tag){
		this.tag = tag;		
	}
	
	public void log(String msg){
		System.out.println(tag +": " + msg);
	}

}
