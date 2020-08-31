package hr.fer.zemris.java.hw16.servlets;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * This servlet provides the client with a big version of a photo
 * given by its name.
 * 
 * @author Ivan Skorupan
 */
@WebServlet(name="fetchPhoto", urlPatterns={"/servlets/fetchPhoto"})
public class FetchPhoto extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		ServletContext context = req.getServletContext();
		String photoName = req.getParameter("name");
		
		String imagePath = context.getRealPath("WEB-INF/slike/" + photoName);
		Path image = Paths.get(imagePath);
		
		resp.setContentType("image/jpg");
		
		InputStream imageInputStream = new BufferedInputStream(Files.newInputStream(image));
		OutputStream os = resp.getOutputStream();
		
		while(true) {
			byte[] buffer = new byte[128];
			int r = imageInputStream.read(buffer);
			if(r < 1) break;
			os.write(buffer, 0, r);
		}
		
		imageInputStream.close();
	}
	
}
