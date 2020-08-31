package hr.fer.zemris.java.hw15.web.forms;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import hr.fer.zemris.java.hw15.model.BlogComment;
import hr.fer.zemris.java.hw15.model.BlogEntry;
import hr.fer.zemris.java.hw15.model.BlogUser;

/**
 * This class models a blog form.
 * 
 * @author Ivan Skorupan
 */
public class BlogForm {
	
	/**
	 * Blog id.
	 */
	private Long id;
	
	/**
	 * List of comments for this blog.
	 */
	private List<BlogComment> comments = new ArrayList<>();
	
	/**
	 * Date on which this blog was created.
	 */
	private Date createdAt;
	
	/**
	 * Date on which this blog was last modified.
	 */
	private Date lastModifiedAt;
	
	/**
	 * Blog's title.
	 */
	private String title;
	
	/**
	 * Blog's content.
	 */
	private String text;
	
	/**
	 * Blog's creator reference.
	 */
	private BlogUser creator;
	
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
	 * Fills this blog form from an HTTP request.
	 * 
	 * @param req - http request object
	 */
	public void fillFromHttpRequest(HttpServletRequest req) {
		this.title = prepare(req.getParameter("title"));
		this.text = prepare(req.getParameter("text"));
	}
	
	/**
	 * Fills this form from a blog entry object.
	 * 
	 * @param blog - blog entry object
	 */
	public void fillFromBlogEntry(BlogEntry blog) {
		this.title = blog.getTitle();
		this.text = blog.getText();
		this.createdAt = blog.getCreatedAt();
		this.lastModifiedAt = blog.getLastModifiedAt();
		this.creator = blog.getCreator();
		this.comments = blog.getComments();
		this.id = blog.getId();
	}
	
	/**
	 * Fills the given blog with information from this form.
	 * 
	 * @param blog - blog entry to fill
	 */
	public void fillBlogEntry(BlogEntry blog) {
		blog.setTitle(title);
		blog.setText(text);
		blog.setComments(comments);
		blog.setCreatedAt(createdAt);
		blog.setLastModifiedAt(lastModifiedAt);
		blog.setCreator(creator);
		blog.setId(id);
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
		
		if(title.isEmpty()) {
			errors.put("title", "Blog title is mandatory!");
		}
		
		if(text.isEmpty()) {
			errors.put("text", "Blog content cannot be empty!");
		}
	}

	/**
	 * Getter for blog title.
	 * 
	 * @return blog title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * Setter for blog title.
	 * 
	 * @param title - blog title
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * Getter for blog content.
	 * 
	 * @return blog content
	 */
	public String getText() {
		return text;
	}

	/**
	 * Setter for blog content.
	 * 
	 * @param text - blog content
	 */
	public void setText(String text) {
		this.text = text;
	}
	
	/**
	 * Getter for blog id.
	 * 
	 * @return blog id
	 */
	public Long getId() {
		return id;
	}
	
	/**
	 * Setter for blog id.
	 * 
	 * @param id - blog id
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * Getter for blog comments.
	 * 
	 * @return blog comments
	 */
	public List<BlogComment> getComments() {
		return comments;
	}
	
	/**
	 * Setter for blog comments.
	 * 
	 * @param comments - blog comments list
	 */
	public void setComments(List<BlogComment> comments) {
		this.comments = comments;
	}

	/**
	 * Getter for blog's creation date.
	 * 
	 * @return blog's creation date
	 */
	public Date getCreatedAt() {
		return createdAt;
	}

	/**
	 * Setter for blog's creation time.
	 * 
	 * @param createdAt - blog's creation time
	 */
	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	/**
	 * Getter for blog's last modified date.
	 * 
	 * @return blog's last modified date
	 */
	public Date getLastModifiedAt() {
		return lastModifiedAt;
	}

	/**
	 * Setter for blog's last modified date.
	 * 
	 * @param lastModifiedAt - blog's last modified date
	 */
	public void setLastModifiedAt(Date lastModifiedAt) {
		this.lastModifiedAt = lastModifiedAt;
	}
	
	/**
	 * Getter for blog's creator.
	 * 
	 * @return blog's creator
	 */
	public BlogUser getCreator() {
		return creator;
	}

	/**
	 * Setter for blog's creator.
	 * 
	 * @param creator - blog's creator
	 */
	public void setCreator(BlogUser creator) {
		this.creator = creator;
	}
	
}
