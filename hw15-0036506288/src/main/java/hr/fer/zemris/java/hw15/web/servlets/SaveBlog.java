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
import hr.fer.zemris.java.hw15.model.BlogEntry;
import hr.fer.zemris.java.hw15.model.BlogUser;
import hr.fer.zemris.java.hw15.web.forms.BlogForm;

/**
 * This servlet handles the persistence of a blog entry that was just created or edited.
 * <p>
 * In case a new blog entry was created, it's properties are validated and if they are correct,
 * the entry is persisted in the database.
 * <p>
 * If an entry was edited, it's form is also validated and if valid, it's content is modified.
 * <p>
 * Otherwise, the client is redirected back to the form with error messages rendered.
 * 
 * @author Ivan Skorupan
 */
@WebServlet(name="save", urlPatterns={"/servleti/save"})
public class SaveBlog extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		process(req, resp);
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		process(req, resp);
	}
	
	/**
	 * This method proccesses the persistence of new or edited blog entries and their validation.
	 * 
	 * @param req - http request object
	 * @param resp - http response object
	 * @throws IOException if there is an IO error
	 * @throws ServletException if there is a problem with servlet proccessing
	 */
	private void process(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
		req.setCharacterEncoding("UTF-8");
		
		String method = req.getParameter("method");
		if(!method.equals("Save")) {
			resp.sendRedirect(resp.encodeRedirectURL(req.getContextPath() + "/servleti/main"));
			return;
		}
		
		String id = req.getParameter("id");
		BlogEntry blog = null;
		if(id != null) {
			blog = DAOProvider.getDAO().getBlogEntry(Long.valueOf(id));
		} else {
			blog = new BlogEntry();
		}
		
		BlogForm form = new BlogForm();
		form.fillFromHttpRequest(req);
		form.validate();
		
		if(form.hasErrors()) {
			req.setAttribute("blog", form);
			req.getRequestDispatcher("/WEB-INF/pages/blogEntryEditing.jsp").forward(req, resp);
			return;
		}
		
		blog.setLastModifiedAt(Date.from(Instant.now()));
		if(blog.getCreatedAt() == null) {
			blog.setCreatedAt(Date.from(Instant.now()));
		}
		if(blog.getCreator() == null) {
			blog.setCreator((BlogUser) req.getSession().getAttribute("current.user")); 
		}
		
		blog.setTitle(form.getTitle());
		blog.setText(form.getText());
		
		if(id == null) {
			EntityManagerFactory emf = (EntityManagerFactory) req.getServletContext().getAttribute("my.application.emf");
			EntityManager em = emf.createEntityManager();
			em.getTransaction().begin();
			
			em.persist(blog);
			em.getTransaction().commit();
			em.close();
		}
		
		resp.sendRedirect(resp.encodeRedirectURL(req.getContextPath() + "/servleti/author/" + req.getSession().getAttribute("current.user.nick")));
	}
	
}
