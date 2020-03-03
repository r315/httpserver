package local;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.Scanner;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletHandler;

/**
*@brief small Http server for register game scores
* date: 20-06-2017
**/

public class Main {
	private static final int defaultPort = 8080;
	private static Logger log;
	private static Server server;	
	
	public static void clearScores(){
		Properties properties = new Properties();
		
		try {			
		  properties.load(new FileInputStream(System.getProperty("user.dir")+"/Config.prop"));
		  (new ScoresFileSupplier(properties.getProperty("datafile"))).clear();		  
		} catch (IOException e) {
			log.log("No Configuration file found");
		}	
	}

	public static void serverInit(int port){
		
		log.log("Hello");
		log.log("Starting server on port " + port);
		
		server = new Server(port);
		ServletHandler handler = new ServletHandler();
		server.setHandler(handler);
		
		handler.addServletWithMapping(FavIconServlet.class, "/favicon.ico");
		handler.addServletWithMapping(TopScoreServletBrowser.class, "/");
		handler.addServletWithMapping(TopScoreServlet.class, "/emb");		
		
		try{
			server.start();
			log.log("Server Start");
		}catch(Exception e){
			log.log("Error: "+ e);			
		}	
	}
	
	private static String parseParameter(String [] args, String param, boolean hasParam){
		for(int i = 0; i < args.length; i++){
			if(args[i].compareTo(param) == 0){
				if(hasParam)
					if( (i+1) < args.length)
						return args[i+1];
					else
						break;
				else
					return args[i];
			}
		}
		return null;
	}
	
	private static void printHelp(){
		System.out.println("Usage: java -jar TopScores.jar [-p port] [-i]\n");
		System.out.println("\t	-p : port");
		System.out.println("\t	-i : interactive mode");
		System.out.println("\n\nAvailable console commands:");
		System.out.println("\t	help");
		System.out.println("\t	exit");
		System.out.println("\t	clear");
	}
	
	public static void main(String [] args){
		boolean done = false;
		String [] cmd;
		Scanner scanner = new Scanner(System.in);
		
		log = new Logger("Main");		
		log.log("Working Directory: " + System.getProperty("user.dir"));
		
		int port;
		try{			
			port = Integer.parseInt(parseParameter(args,"-p", true));
		}catch(Exception e){
			port = defaultPort;			
		}
		
		serverInit(port);	
		
		try {
			if(parseParameter(args,"-i",false) == null){
				done = true;
				server.join();			
			}
		} catch (InterruptedException e) {
			log.log("Fail join to server > "+ e);
		}		
		
		while(!done){
			System.out.println("Insert command: ");
			cmd =  scanner.nextLine().split(" ");
			
			switch(cmd[0].toLowerCase()){
			case "exit":
				done = true;
				break;
				
			case "clear":
				clearScores();
				break;
			case "help":
				printHelp();
				break;
			}			
		}
		
		scanner.close();
		
		log.log("Exiting..");
		try {
			server.stop();
		} catch (Exception e) {
			log.log(e.toString());
		}		
		
	}

}

