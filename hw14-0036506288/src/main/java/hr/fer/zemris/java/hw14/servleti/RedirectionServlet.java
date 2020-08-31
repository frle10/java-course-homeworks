package hr.fer.zemris.java.hw14.servleti;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * The idea of this servlet is for it to be mapped to "/index.html" so that a user
 * upon entering this web application triggers it by default.
 * <p>
 * The servlet itself does nothing but redirect the client computer to trigger a call
 * to "servleti/index.html" where actual data is first presented to the user.
 * 
 * @author Ivan Skorupan
 */
@WebServlet(name="redirection", urlPatterns={"/index.html"})
public class RedirectionServlet extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		resp.sendRedirect(resp.encodeRedirectURL(req.getContextPath() + "/servleti/index.html"));
	}
	
}
