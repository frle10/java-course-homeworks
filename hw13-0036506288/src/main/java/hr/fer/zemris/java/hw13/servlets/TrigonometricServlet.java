package hr.fer.zemris.java.hw13.servlets;

import static java.lang.Math.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * This servlet will parse two parameters a and b as integers and will calculate
 * sines and cosines of all values from a to b and will prepare those results in
 * two lists. The lists will then be set as request parameters and further action
 * is delegated to an appropriate .jsp file for result rendering to the client.
 * 
 * @author Ivan Skorupan
 */
public class TrigonometricServlet extends HttpServlet {
	
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		int a = 0;
		int b = 360;
		
		try {
			a = Integer.parseInt(req.getParameter("a"));
		} catch(Exception ignorable) {
		}
		
		try {
			b = Integer.parseInt(req.getParameter("b"));
		} catch(Exception ignorable) {
		}
		
		if(a > b) {
			int temp = a;
			a = b;
			b = temp;
		}
		
		if(b > a + 720) {
			b = a + 720;
		}
		
		List<Double> sines = new ArrayList<>();
		List<Double> cosines = new ArrayList<>();
		for(int i = a; i <= b; i++) {
			sines.add(sin(toRadians(i)));
			cosines.add(cos(toRadians(i)));
		}
		
		req.setAttribute("a", a);
		req.setAttribute("b", b);
		req.setAttribute("sines", sines);
		req.setAttribute("cosines", cosines);
		req.getRequestDispatcher("/WEB-INF/pages/trigonometric.jsp").forward(req, resp);
	}
	
}
