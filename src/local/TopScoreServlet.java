package local;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.Properties;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class TopScoreServlet extends HttpServlet {
	
	/**
	 * 
	 */
	private static final String propFile = "/Config.prop";
	private static final long serialVersionUID = -674452397517443217L;	
	private String baseDir;
	private String dataFile;
	
	private Logger log;
	
	public TopScoreServlet(){
		baseDir = System.getProperty("user.dir");		
		Properties properties = new Properties();
		
		try {			
		  properties.load(new FileInputStream(baseDir + propFile));
		  dataFile = properties.getProperty("datafile");		  
		} catch (IOException e) {
			log.log("No Configuration file found");
		}	
	}

	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp){
		ResourseSupplier rs = new ScoresFileSupplier(baseDir + "/" + dataFile);		
	       
		try {		
			String respBody = rs.readData().stream()
					.filter(s -> s.length() > 0)										
					.map(s -> Score.mapToScore(s))
					.sorted(new Score() :: compare)					
					.map(s -> s.toString())					
					.collect(Collectors.joining("\n"));					
			doResponse(resp, respBody);
		} catch (IOException e) {
			log.log("Error Reading Data File > " + e);		
		}
	}
	
	@Override	
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		ResourseSupplier rs = new ScoresFileSupplier(dataFile);
		
		try{
			int score = Integer.parseInt(req.getParameter("pscore"));
			score = score % Score.MAX_SCORE;
			rs.writeData(req.getParameter("pname") + " " + Integer.toString(score) +"\n");
		}catch(Exception e){
			log.log("Bad parameters for post");
		}
				
		//resp.sendRedirect("/");		
	}
	
	private void doResponse(HttpServletResponse resp, String page) throws IOException {
        Charset utf8 = Charset.forName("utf-8");
        resp.setContentType(String.format("text/html; charset=%s",utf8.name()));
        byte[] respBodyBytes = page.getBytes(utf8);
        resp.setContentLength(respBodyBytes.length);
        OutputStream os = resp.getOutputStream();
        os.write(respBodyBytes);
        os.close();
}
}
