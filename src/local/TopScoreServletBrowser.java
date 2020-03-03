package local;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.StringWriter;
import java.io.Writer;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;


public class TopScoreServletBrowser extends HttpServlet {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -1572343382670472161L;
	private static final String propFile = "/Config.prop";
	private String templateFile;
	private String baseDir;	
	private String dataFile;
	
	private Configuration cfg;
	private Logger log; 
	
	public TopScoreServletBrowser(){		
		baseDir = System.getProperty("user.dir");		
		Properties properties = new Properties();
		
		cfg = new Configuration(Configuration.VERSION_2_3_26);
		log = new Logger("Servlet");
		
		
		try {			
		  properties.load(new FileInputStream(baseDir + propFile));
		  dataFile = properties.getProperty("datafile");
		  templateFile = properties.getProperty("templatefile");
		} catch (IOException e) {
			log.log("No Configuration file found");
		}	
		
		
		try {
			cfg.setDirectoryForTemplateLoading(new File(baseDir));
		} catch (IOException e) {
			log.log("Error Setting Template Directory > " + e);			
		}
		cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
		cfg.setLogTemplateExceptions(false);
		cfg.setDefaultEncoding("UTF-8");
	}	
	
	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp){
		ResourseSupplier rs = new ScoresFileSupplier(baseDir + "/" + dataFile);
		Template template = null;
		
		Map<String,Object> root = new HashMap<String,Object>();		
		root.put("title","Scores Table");	
		
		try {
			template = cfg.getTemplate(templateFile);
		} catch (IOException e) {
			log.log("Error Opening Template File > " + e);
		}	       
	       
		try (Writer out = new StringWriter()){		
			root.put("scores", rs.readData().stream()
					.filter(s -> s.length() > 0)					
					.map(s -> Score.mapToScore(s))
					.sorted(new Score() :: compare)
					.collect(Collectors.toList())
					);
			
			template.process(root, out);			
			doResponse(resp,out.toString());
		} catch (IOException e) {
			log.log("Error Reading Template File > " + e);
		} catch (TemplateException e) {
			log.log("Error Creating Output from Template > " + e);
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
		resp.sendRedirect("/");		
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
