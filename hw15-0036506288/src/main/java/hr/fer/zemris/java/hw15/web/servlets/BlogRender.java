package hr.fer.zemris.java.hw15.web.servlets;

import hr.fer.zemris.java.hw15.dao.DAOProvider;
import hr.fer.zemris.java.hw15.model.BlogEntry;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * This servlet prepares a blog with given id for rendering in a .jsp file.
 * 
 * @author Ivan Skorupan
 */
@WebServlet(name="blogRendering", urlPatterns={"/servleti/renderBlog"})
public class BlogRender extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String sID = req.getParameter("id");
		Long id = null;
		try {
			id = Long.valueOf(sID);
		} catch(Exception ignorable) {
		}
		
		if(id != null) {
			BlogEntry blogEntry = DAOProvider.getDAO().getBlogEntry(id);
			if(blogEntry != null) {
				req.setAttribute("blogEntry", blogEntry);
			}
		}
		
		req.getRequestDispatcher("/WEB-INF/pages/renderBlog.jsp").forward(req, resp);
	}
	
}