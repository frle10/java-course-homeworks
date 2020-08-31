package hr.fer.zemris.java.hw15.web.servlets;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * This filter prepares a string saying if the user is logged in or not.
 * <p>
 * This is convenient because we need to show this string on every site in its header
 * and with a filter we can put this code in one place instead of in every .jsp file.
 * 
 * @author Ivan Skorupan
 */
@WebFilter(filterName="loggedIn", urlPatterns={"/servleti/*"})
public class LoggedInFilter implements Filter {

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) request;
		HttpSession session = req.getSession();
		
		Long id = (Long) session.getAttribute("current.user.id");
		String message = null;
		if(id == null) {
			message = "Not logged in.";
		} else {
			message = session.getAttribute("current.user.fn") + " " +
				session.getAttribute("current.user.ln") + " is logged in.";
		}
		
		request.setAttribute("loggedIn", message);
		chain.doFilter(request, response);
	}

	@Override
	public void destroy() {
	}

}
