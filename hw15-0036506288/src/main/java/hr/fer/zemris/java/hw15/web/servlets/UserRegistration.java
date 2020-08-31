package hr.fer.zemris.java.hw15.web.servlets;

import java.io.IOException;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import hr.fer.zemris.java.hw15.model.BlogUser;
import hr.fer.zemris.java.hw15.web.forms.UserForm;

/**
 * This servlet handles user registration.
 * <p>
 * The registration form is being filled from the HTTP request object and
 * is then being validated. If it is not valid, the user is redirected back to the
 * form with error messages set.
 * <p>
 * If it is valid, the user's data is persisted in the database and they are
 * successfully registered.
 * 
 * @author Ivan Skorupan
 */
@WebServlet(name="register", urlPatterns={"/servleti/register"})
public class UserRegistration extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		Long currentId = (Long) req.getSession().getAttribute("current.user.id");
		if(currentId != null) {
			req.setAttribute("errorMsg", "You are already registered and logged in!");
			req.getRequestDispatcher("/WEB-INF/pages/error.jsp").forward(req, resp);
			return;
		}
		
		req.getRequestDispatcher("/WEB-INF/pages/registration.jsp").forward(req, resp);
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.setCharacterEncoding("UTF-8");
		
		if(req.getParameter("method").equals("Cancel")) {
			resp.sendRedirect(resp.encodeRedirectURL(req.getContextPath() + "/servleti/main"));
			return;
		}
		
		UserForm form = new UserForm();
		form.fillFromHttpRequest(req);
		form.validate();
		
		if(form.hasErrors()) {
			req.setAttribute("form", form);
			req.getRequestDispatcher("/WEB-INF/pages/registration.jsp").forward(req, resp);
			return;
		}
		
		BlogUser user = new BlogUser();
		form.fillBlogUser(user);
		
		EntityManagerFactory emf = (EntityManagerFactory) req.getServletContext().getAttribute("my.application.emf");
		EntityManager em = emf.createEntityManager();
		
		em.getTransaction().begin();
		em.persist(user);
		em.getTransaction().commit();
		em.close();
		
		req.getRequestDispatcher("/WEB-INF/pages/registerSuccess.jsp").forward(req, resp);
	}
	
}
