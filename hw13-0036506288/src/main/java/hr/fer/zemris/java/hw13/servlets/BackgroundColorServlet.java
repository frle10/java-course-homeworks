package hr.fer.zemris.java.hw13.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * This servlet is used to set the background color on every page in this web application.
 * 
 * @author Ivan Skorupan
 */
public class BackgroundColorServlet extends HttpServlet {
	
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String selectedColor = req.getParameter("pickedBgCol");
		selectedColor = (selectedColor == null) ? "white" : selectedColor;
		
		HttpSession session = req.getSession();
		session.setAttribute("pickedBgCol", selectedColor);
		
		resp.sendRedirect(resp.encodeRedirectURL(req.getContextPath() + "/index.jsp"));
	}
	
}
