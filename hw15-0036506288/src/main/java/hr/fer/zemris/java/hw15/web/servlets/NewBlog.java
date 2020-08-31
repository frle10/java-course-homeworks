package hr.fer.zemris.java.hw15.web.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import hr.fer.zemris.java.hw15.model.BlogEntry;
import hr.fer.zemris.java.hw15.web.forms.BlogForm;

/**
 * This servlet prepares some data for a .jsp file that renders a form for making a new
 * blog entry.
 * 
 * @author Ivan Skorupan
 */
@WebServlet(name="new", urlPatterns={"/servleti/new"})
public class NewBlog extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		BlogEntry blog = new BlogEntry();
		BlogForm form = new BlogForm();
		form.fillFromBlogEntry(blog);
		
		req.setAttribute("blog", form);
		req.getRequestDispatcher("/WEB-INF/pages/blogEntryEditing.jsp").forward(req, resp);
	}
}
