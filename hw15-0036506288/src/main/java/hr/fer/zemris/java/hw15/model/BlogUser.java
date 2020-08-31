package hr.fer.zemris.java.hw15.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

/**
 * This class models a single blog user.
 * <p>
 * Each blog user has an auto-generated id, first name,
 * last name, nickname, email, a password in its hashed
 * form and a list of blog entries.
 * 
 * @author Ivan Skorupan
 */
@Entity
@Table(name="blog_users", uniqueConstraints=
	{@UniqueConstraint(columnNames={"nick"})})
public class BlogUser {
	
	/**
	 * Blog user's id.
	 */
	private Long id;
	
	/**
	 * Blog user's first name.
	 */
	private String firstName;
	
	/**
	 * Blog user's last name.
	 */
	private String lastName;
	
	/**
	 * Blog user's nickname.
	 */
	private String nick;
	
	/**
	 * Blog user's email.
	 */
	private String email;
	
	/**
	 * Blog user's password hash.
	 */
	private String passwordHash;
	
	/**
	 * List of blog entries of this user.
	 */
	private List<BlogEntry> blogEntries = new ArrayList<>();
	
	/**
	 * Getter for user id.
	 * 
	 * @return user id
	 */
	@Id @GeneratedValue
	public Long getId() {
		return id;
	}
	
	/**
	 * Setter for user id.
	 * 
	 * @param id - user id
	 */
	public void setId(Long id) {
		this.id = id;
	}
	
	/**
	 * Getter for user's first name.
	 * 
	 * @return user's first name
	 */
	@Column(length=30,nullable=false)
	public String getFirstName() {
		return firstName;
	}
	
	/**
	 * Setter for user's first name.
	 * 
	 * @param firstName - user's first name
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	
	/**
	 * Getter for user's last name.
	 * 
	 * @return user's last name
	 */
	@Column(length=30,nullable=false)
	public String getLastName() {
		return lastName;
	}
	
	/**
	 * Setter for user's last name.
	 * 
	 * @param lastName - user's last name
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	
	/**
	 * Getter for user's nickname.
	 * 
	 * @return user's nickname
	 */
	@Column(length=30,nullable=false)
	public String getNick() {
		return nick;
	}
	
	/**
	 * Setter for user's nickname.
	 * 
	 * @param nick - user's nickname
	 */
	public void setNick(String nick) {
		this.nick = nick;
	}
	
	/**
	 * Getter for user's email.
	 * 
	 * @return user's email
	 */
	@Column(length=100,nullable=false)
	public String getEmail() {
		return email;
	}
	
	/**
	 * Setter for user's email.
	 * 
	 * @param email - user's email
	 */
	public void setEmail(String email) {
		this.email = email;
	}
	
	/**
	 * Getter for user's password hash.
	 * 
	 * @return user's password hash
	 */
	public String getPasswordHash() {
		return passwordHash;
	}
	
	/**
	 * Setter for user's password hash.
	 * 
	 * @param passwordHash - user's password hash
	 */
	public void setPasswordHash(String passwordHash) {
		this.passwordHash = passwordHash;
	}
	
	/**
	 * Getter for user's blog entries.
	 * 
	 * @return user's blog entries
	 */
	@Column(nullable=false)
	@OneToMany(mappedBy="creator")
	public List<BlogEntry> getBlogEntries() {
		return blogEntries;
	}

	/**
	 * Setter for user's blog entries.
	 * 
	 * @param blogEntries - user's blog entries list
	 */
	public void setBlogEntries(List<BlogEntry> blogEntries) {
		this.blogEntries = blogEntries;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof BlogUser))
			return false;
		BlogUser other = (BlogUser) obj;
		return Objects.equals(id, other.id);
	}
	
}
