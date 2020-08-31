package hr.fer.zemris.java.hw15.web.servlets;

import java.io.IOException;
import java.time.Instant;
import java.util.Date;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import hr.fer.zemris.java.hw15.dao.DAOProvider;
import hr.fer.zemris.java.hw15.model.BlogComment;
import hr.fer.zemris.java.hw15.model.BlogEntry;
import hr.fer.zemris.java.hw15.web.forms.CommentForm;

/**
 * This servlet analyzes comment properties submitted through HTTP post method
 * and then performs an appropriate operation based on the analysis.
 * <p>
 * Either the comment is invalid and then the user is redirected back to the form with
 * error messages set or the comment is valid and is therefore persisted in the database
 * and rendered on the blog page.
 * 
 * @author Ivan Skorupan
 */
@WebServlet(name="comment", urlPatterns={"/servleti/addComment"})
public class AddComment extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.setCharacterEncoding("UTF-8");
		
		EntityManagerFactory emf = (EntityManagerFactory) req.getServletContext().getAttribute("my.application.emf");
		EntityManager em = emf.createEntityManager();
		
		String blogEntryID = req.getParameter("id");
		BlogEntry blog = DAOProvider.getDAO().getBlogEntry(Long.valueOf(blogEntryID));
		CommentForm form = new CommentForm();
		form.fillFromHttpRequest(req);
		form.validate();
		
		if(form.hasErrors()) {
			System.out.println(form.getError("email"));
			System.out.println(form.getError("comment"));
			req.setAttribute("form", form);
			req.setAttribute("blogEntry", blog);
			req.getRequestDispatcher("/WEB-INF/pages/renderBlog.jsp").forward(req, resp);
			return;
		}
		
		String commentText = req.getParameter("comment");
		String email = req.getParameter("email");
		
		BlogComment comment = new BlogComment();
		comment.setMessage(commentText);
		comment.setPostedOn(Date.from(Instant.now()));
		comment.setUsersEMail(email);
		comment.setBlogEntry(blog);
		
		em.getTransaction().begin();
		em.persist(comment);
		em.getTransaction().commit();
		em.close();
		
		req.setAttribute("blogEntry", blog);
		resp.sendRedirect(resp.encodeRedirectURL(req.getContextPath() + "/servleti/author/" +
				blog.getCreator().getNick() + "/" + blogEntryID));
	}
	
}
