package hr.fer.zemris.java.hw15.web.servlets;

import java.io.IOException;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import hr.fer.zemris.java.hw15.dao.DAOProvider;
import hr.fer.zemris.java.hw15.model.BlogEntry;

/**
 * This servlet can be triggered by several different sites that perform
 * different functions. Therefore, it needs to check the path that triggered it
 * and dispatch the work to an appropriate "sub-servlet" that will prepare content
 * for the client.
 * <p>
 * This servlet will dispatch the work only to servlets that prepare creation or
 * editing of a blog entry. Also, it can redirect to a servlet that renders a specific
 * blog with given id.
 * <p>
 * Otherwise, this servlet's function is to prepare a list of all blogs by a certain author
 * for a .jsp to render.
 * 
 * @author Ivan Skorupan
 */
@WebServlet(name="author-blogs", urlPatterns={"/servleti/author/*"})
public class AuthorBlogs extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String pathInfo = req.getPathInfo();
		String cause = pathInfo.substring(pathInfo.lastIndexOf("/") + 1);
		
		try {
			Long id = Long.parseLong(cause);
			req.setAttribute("nick", pathInfo.substring(0, pathInfo.lastIndexOf("/")));
			req.getRequestDispatcher("/servleti/renderBlog?id=" + id).forward(req, resp);
			return;
		} catch(NumberFormatException ignorable) {
		}
		
		if(cause.equals("new") || cause.equals("edit")) {
			String nick = pathInfo.substring(1, pathInfo.lastIndexOf("/"));
			if(!nick.equals(req.getSession().getAttribute("current.user.nick"))) {
				resp.sendRedirect(resp.encodeRedirectURL(req.getContextPath() + "/servleti/main"));
				return;
			}
			
			String blogID = req.getParameter("id");
			if(blogID != null) {
				BlogEntry currentBlog = DAOProvider.getDAO().getBlogEntry(Long.valueOf(blogID));
				if(!currentBlog.getCreator().getNick().equals(req.getSession().getAttribute("current.user.nick"))) {
					resp.sendRedirect(resp.encodeRedirectURL(req.getContextPath() + "/servleti/main"));
					return;
				}
			}
			
			req.getRequestDispatcher("/servleti/" + cause).forward(req, resp);
			return;
		}
		
		EntityManagerFactory emf = (EntityManagerFactory) req.getServletContext().getAttribute("my.application.emf");
		EntityManager em = emf.createEntityManager();
		em.getTransaction().begin();
		
		String nick = req.getRequestURI().substring(req.getRequestURI().lastIndexOf("/") + 1);
		List<BlogEntry> blogs = em.createQuery("SELECT b FROM BlogEntry AS b WHERE creator.nick=:nick", BlogEntry.class)
				.setParameter("nick", nick)
				.getResultList();
		
		em.getTransaction().commit();
		em.close();
		
		req.setAttribute("nick", nick);
		req.setAttribute("blogs", blogs);
		req.getRequestDispatcher("/WEB-INF/pages/authorBlogs.jsp").forward(req, resp);
	}
	
}
