package hr.fer.zemris.java.hw15.web.forms;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import hr.fer.zemris.java.hw15.model.BlogComment;
import hr.fer.zemris.java.hw15.model.BlogEntry;

/**
 * This class models a comment form.
 * 
 * @author Ivan Skorupan
 */
public class CommentForm {
	
	/**
	 * Comment id.
	 */
	private Long id;
	
	/**
	 * Comment's corresponding blog entry.
	 */
	private BlogEntry blogEntry;
	
	/**
	 * Email of user who wrote the comment.
	 */
	private String usersEMail;
	
	/**
	 * Comment content.
	 */
	private String message;
	
	/**
	 * Date on which this comment was posted on.
	 */
	private Date postedOn;
	
	/**
	 * This form's errors map.
	 */
	Map<String, String> errors = new HashMap<>();
	
	/**
	 * Gets the error message for given key.
	 * 
	 * @param key - error key
	 * @return error message for given key
	 */
	public String getError(String key) {
		return errors.get(key);
	}
	
	/**
	 * Tests if this form contains any errors.
	 * 
	 * @return <code>true</code> if there are errors, <code>false</code> otherwise
	 */
	public boolean hasErrors() {
		return !errors.isEmpty();
	}
	
	/**
	 * Tests if this form has an error with given key. 
	 * 
	 * @param key - error key
	 * @return <code>true</code> if error with given key exists, <code>false</code> otherwise
	 */
	public boolean hasError(String key) {
		return errors.containsKey(key);
	}
	
	/**
	 * Fills this comment form from an HTTP request.
	 * 
	 * @param req - http request object
	 */
	public void fillFromHttpRequest(HttpServletRequest req) {
		this.usersEMail = prepare(req.getParameter("email"));
		this.message = prepare(req.getParameter("comment"));
	}
	
	/**
	 * Fills this form from a blog comment object.
	 * 
	 * @param comment - blog comment object
	 */
	public void fillFromBlogComment(BlogComment comment) {
		this.blogEntry = comment.getBlogEntry();
		this.id = comment.getId();
		this.message = comment.getMessage();
		this.postedOn = comment.getPostedOn();
		this.usersEMail = comment.getUsersEMail();
	}
	
	/**
	 * Fills the given comment with information from this form.
	 * 
	 * @param comment - comment to fill
	 */
	public void fillBlogComment(BlogComment comment) {
		comment.setBlogEntry(blogEntry);
		comment.setId(id);
		comment.setMessage(message);
		comment.setPostedOn(postedOn);
		comment.setUsersEMail(usersEMail);
	}
	
	/**
	 * Prepares strings from http request for saving into this form.
	 * 
	 * @param text - string to prepare
	 * @return prepared string
	 */
	public String prepare(String text) {
		if(text == null) return "";
		return text.trim();
	}
	
	/**
	 * Validates the form, if there are errors they are being put in the errors map.
	 */
	public void validate() {
		errors.clear();
		
		if(message.isEmpty()) {
			errors.put("comment", "A comment cannot be empty!");
		}
		
		if(usersEMail.isEmpty()) {
			errors.put("email", "E-mail is mandatory!");
		} else {
			int l = usersEMail.length();
			int p = usersEMail.indexOf('@');
			if(l < 3 || p == -1 || p == 0 || p == l - 1) {
				errors.put("email", "E-mail address has invalid format!");
			}
		}
	}
	
	/**
	 * Getter for comment id.
	 * 
	 * @return comment id
	 */
	public Long getId() {
		return id;
	}
	
	/**
	 * Setter for comment id.
	 * 
	 * @param id - comment id
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * Getter for comment's blog entry.
	 * 
	 * @return comment's blog entry
	 */
	public BlogEntry getBlogEntry() {
		return blogEntry;
	}
	
	/**
	 * Setter for comment's blog entry.
	 * 
	 * @param blogEntry - comment's blog entry
	 */
	public void setBlogEntry(BlogEntry blogEntry) {
		this.blogEntry = blogEntry;
	}

	/**
	 * Getter for comment user's email.
	 * 
	 * @return comment user's email
	 */
	public String getUsersEMail() {
		return usersEMail;
	}

	/**
	 * Setter for comment user's email.
	 * 
	 * @param usersEMail - comment user's email
	 */
	public void setUsersEMail(String usersEMail) {
		this.usersEMail = usersEMail;
	}

	/**
	 * Getter for comment content.
	 * 
	 * @return comment content
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * Setter for comment content.
	 * 
	 * @param message - comment content
	 */
	public void setMessage(String message) {
		this.message = message;
	}

	/**
	 * Getter for comment date.
	 * 
	 * @return comment date
	 */
	public Date getPostedOn() {
		return postedOn;
	}

	/**
	 * Setter for comment's date.
	 * 
	 * @param postedOn - comment's date
	 */
	public void setPostedOn(Date postedOn) {
		this.postedOn = postedOn;
	}
	
}
