package hr.fer.zemris.java.hw15.web.servlets;

import static hr.fer.zemris.java.hw15.web.servlets.Util.*;

import java.io.IOException;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import hr.fer.zemris.java.hw15.model.BlogUser;

/**
 * This servlet handles user authentication. The provided username and password
 * are fetched and compared with an entry in the database for those values.
 * <p>
 * If no entry for provided nickname was found or if it was found but the password hash
 * does not match, the user is redirected back to the login form with an error message rendered.
 * <p>
 * Otherwise, relevant user data is prepared in session attributes and a redirection back to homepage
 * happens.
 * 
 * @author Ivan Skorupan
 */
@WebServlet(name="authenticate", urlPatterns={"/servleti/authenticate"})
public class UserAuthentication extends HttpServlet {
	
	private static final long serialVersionUID = 1L;

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.setCharacterEncoding("UTF-8");
		
		HttpSession session = req.getSession();
		EntityManagerFactory emf = (EntityManagerFactory) req.getServletContext().getAttribute("my.application.emf");
		EntityManager em = emf.createEntityManager();
		String nickname = req.getParameter("nick");
		String password = req.getParameter("password");
		
		em.getTransaction().begin();
		List<BlogUser> fetchedUsers = em.createQuery("SELECT u FROM BlogUser AS u WHERE u.nick=:nick", BlogUser.class)
				.setParameter("nick", nickname)
				.getResultList();
		em.getTransaction().commit();
		em.close();
		
		if(fetchedUsers == null || fetchedUsers.isEmpty()) {
			req.setAttribute("errorMsg", "Wrong username and/or password!");
			req.setAttribute("oldNick", nickname);
			req.getRequestDispatcher("/WEB-INF/pages/index.jsp").forward(req, resp);
			return;
		}
		
		BlogUser fetchedUser = fetchedUsers.get(0);
		
		String encodedPassword = null;
		encodedPassword = byteToHex(digestString(password));
		
		if(encodedPassword.equals(fetchedUser.getPasswordHash())) {
			session.setAttribute("current.user.id", fetchedUser.getId());
			session.setAttribute("current.user.nick", fetchedUser.getNick());
			session.setAttribute("current.user.fn", fetchedUser.getFirstName());
			session.setAttribute("current.user.ln", fetchedUser.getLastName());
			session.setAttribute("current.user.email", fetchedUser.getEmail());
			session.setAttribute("current.user", fetchedUser);
			resp.sendRedirect(resp.encodeRedirectURL(req.getContextPath() + "/servleti/main"));
		} else {
			req.setAttribute("oldNick", nickname);
			req.setAttribute("errorMsg", "Wrong username and/or password!");
			req.getRequestDispatcher("/WEB-INF/pages/index.jsp").forward(req, resp);
		}
	}
	
}
