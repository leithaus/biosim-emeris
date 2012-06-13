package biosim.server;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.Enumeration;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class ProcessManagerProxyServlet extends HttpServlet {

	UrlX _serviceUrl = new UrlX("http://localhost:8080/api/");
	
	private static final long serialVersionUID = 1L;
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String serviceParms = getUrlParms(req);
		URL url = new URL(_serviceUrl.subPath(req.getPathInfo()).asString() + serviceParms);
		URLConnection conn = url.openConnection();
		conn.connect();
		InputStream in = conn.getInputStream();
		byte[] buffer = new byte[8192];
		while ( true ) {
			int readCount = in.read(buffer);
			if ( readCount == -1 ) break;
			resp.getOutputStream().write(buffer, 0, readCount);
		}
		in.close();
		resp.getOutputStream().flush();
	}

	String getUrlParms(HttpServletRequest req) throws UnsupportedEncodingException {
		StringBuilder sb = new StringBuilder();
		Enumeration<?> e = req.getParameterNames();
		while ( e.hasMoreElements() ) {
			String parm = e.nextElement().toString();
			String value = req.getParameter(parm);
			if ( sb.length() > 0 ) {
				sb.append("&");
			} else {
				sb.append("?");
			}
			sb.append(parm);
			sb.append("=");
			sb.append(URLEncoder.encode(value, "UTF-8"));
		}
		return sb.toString();
	}
	
}
