package hr.fer.zemris.java.hw15.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * This class is a model of a comment on a certain blog.
 * <p>
 * Each comment has its auto-generated id, its corresponding blog entry,
 * the email of a user who wrote the comment, its content and a date
 * it was posted on.
 * 
 * @author Ivan Skorupan
 */
@Entity
@Table(name="blog_comments")
public class BlogComment {

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
	 * Getter for comment id.
	 * 
	 * @return comment id
	 */
	@Id @GeneratedValue
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
	@ManyToOne
	@JoinColumn(nullable=false)
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
	@Column(length=100,nullable=false)
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
	@Column(length=4096,nullable=false)
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
	@Temporal(TemporalType.TIMESTAMP)
	@Column(nullable=false)
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		BlogComment other = (BlogComment) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
}