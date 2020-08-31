package hr.fer.zemris.java.hw15.web.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * This servlet is used to redirect the user to starting point of web application
 * when they try to access it through /web-app-name.
 * 
 * @author Ivan Skorupan
 */
@WebServlet(name="index", urlPatterns={"/index.jsp"})
public class IndexRedirection extends HttpServlet {
	
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		resp.sendRedirect(resp.encodeRedirectURL(req.getContextPath() + "/servleti/main"));
	}
	
}
