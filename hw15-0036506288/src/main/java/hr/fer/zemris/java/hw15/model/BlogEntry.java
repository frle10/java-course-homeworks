package hr.fer.zemris.java.hw15.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Cacheable;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * This class models a single blog entry.
 * <p>
 * Each blog entry has its auto-generated id, a list of comments,
 * dates when the blog was created and last modified, the title
 * and its creator.
 * 
 * @author Ivan Skorupan
 */
@NamedQueries({
	@NamedQuery(name="BlogEntry.upit1",query="select b from BlogComment as b where b.blogEntry=:be and b.postedOn>:when")
})
@Entity
@Table(name="blog_entries")
@Cacheable(false)
public class BlogEntry {

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
	 * Getter for blog id.
	 * 
	 * @return blog id
	 */
	@Id @GeneratedValue
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
	@OneToMany(mappedBy="blogEntry",fetch=FetchType.LAZY, cascade=CascadeType.PERSIST, orphanRemoval=true)
	@OrderBy("postedOn")
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
	@Temporal(TemporalType.TIMESTAMP)
	@Column(nullable=false)
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
	@Temporal(TemporalType.TIMESTAMP)
	@Column(nullable=true)
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
	 * Getter for blog title.
	 * 
	 * @return blog title
	 */
	@Column(length=200,nullable=false)
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
	@Column(length=4096,nullable=false)
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
	 * Getter for blog's creator.
	 * 
	 * @return blog's creator
	 */
	@ManyToOne
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
		BlogEntry other = (BlogEntry) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
}