package hr.fer.zemris.java.hw15.web.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import hr.fer.zemris.java.hw15.dao.DAOProvider;
import hr.fer.zemris.java.hw15.model.BlogEntry;
import hr.fer.zemris.java.hw15.web.forms.BlogForm;

/**
 * This servlet prepares data for blog entry editing.
 * <p>
 * The prepared data is used in a .jsp file that renders a form for editing
 * the blog entry.
 * 
 * @author Ivan Skorupan
 */
@WebServlet(name="edit", urlPatterns={"/servleti/edit"})
public class EditBlog extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		Long id = null;
		try {
			id = Long.valueOf(req.getParameter("id"));
		} catch(NumberFormatException ignorable) {
			req.setAttribute("errorMsg", "Wrong parameter was received!");
			req.getRequestDispatcher("/WEB-INF/pages/error.jsp").forward(req, resp);
		}
		
		BlogEntry blog = DAOProvider.getDAO().getBlogEntry(id);
		if(blog == null) {
			req.setAttribute("errorMsg", "The requested blog does not exist!");
			req.getRequestDispatcher("/WEB-INF/pages/error.jsp").forward(req, resp);
			return;
		}
		
		BlogForm form = new BlogForm();
		form.fillFromBlogEntry(blog);
		req.setAttribute("blog", form);
		req.getRequestDispatcher("/WEB-INF/pages/blogEntryEditing.jsp").forward(req, resp);
	}
}
