package hr.fer.zemris.java.zi.servlets;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name="addImage", urlPatterns={"/addImage"})
public class AddImageServlet extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String fileName = req.getParameter("fileName");
		String content = req.getParameter("content");
		
		if(fileName == null || fileName.isEmpty()) {
			req.getRequestDispatcher("/WEB-INF/pages/greska.jsp").forward(req, resp);
			return;
		}
		
		String extension = fileName.substring(fileName.lastIndexOf(".") + 1);
		if(!extension.equals("jvd")) {
			req.getRequestDispatcher("/WEB-INF/pages/greska.jsp").forward(req, resp);
			return;
		}
		
		for(int i = 0; i < fileName.length(); i++) {
			char znak = fileName.charAt(i);
			if(!Character.isDigit(znak) && !(znak >= 'A' && znak <= 'Z') && !(znak >= 'a' && znak <= 'z') && !(znak == '.')) {
				req.getRequestDispatcher("/WEB-INF/pages/greska.jsp").forward(req, resp);
				return;
			}
		}
		
		Path imagePath = Paths.get(req.getServletContext().getRealPath("/WEB-INF/images/" + fileName));
		OutputStream os = Files.newOutputStream(imagePath);
		os.write(content.getBytes());
		
		resp.sendRedirect(resp.encodeRedirectURL(req.getContextPath() + "/main"));
	}
	
}
