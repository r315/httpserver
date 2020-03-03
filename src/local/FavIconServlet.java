package local;

import java.io.IOException;
import java.nio.charset.Charset;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;



public class FavIconServlet extends HttpServlet {
	 /**
	 * 
	 */
	private static final long serialVersionUID = -2034219321218429213L;

	@Override
	    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
	        Charset utf8 = Charset.forName("utf-8");
	        resp.setContentType(String.format("image/png; charset=%s",utf8.name()));
	        resp.setStatus(404);
	}
}
