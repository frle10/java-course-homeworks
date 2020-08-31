package hr.fer.zemris.java.hw15.web.forms;

import static hr.fer.zemris.java.hw15.web.servlets.Util.*;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import hr.fer.zemris.java.hw15.model.BlogUser;

/**
 * This class models a user form.
 * 
 * @author Ivan Skorupan
 */
public class UserForm {
	
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
	 * Flag that indicates if the password was empty before digesting and encoding.
	 */
	private boolean isPasswordEmpty;
	
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
		this.firstName = prepare(req.getParameter("fn"));
		this.lastName = prepare(req.getParameter("ln"));
		this.nick = prepare(req.getParameter("nick"));
		this.email = prepare(req.getParameter("email"));
		this.passwordHash = byteToHex(digestString(prepare(req.getParameter("password"))));
	}
	
	/**
	 * Fills this form from a blog user object.
	 * 
	 * @param user - blog user object
	 */
	public void fillFromBlogUser(BlogUser user) {
		this.firstName = user.getFirstName();
		this.lastName = user.getLastName();
		this.nick = user.getNick();
		this.email = user.getEmail();
		this.passwordHash = user.getPasswordHash();
	}
	
	/**
	 * Fills the given blog user with information from this form.
	 * 
	 * @param user - blog user to fill
	 */
	public void fillBlogUser(BlogUser user) {
		user.setFirstName(firstName);
		user.setLastName(lastName);
		user.setNick(nick);
		user.setEmail(email);
		user.setPasswordHash(passwordHash);
	}
	
	/**
	 * Prepares strings from http request for saving into this form.
	 * 
	 * @param text - string to prepare
	 * @return prepared string
	 */
	public String prepare(String text) {
		if(text == null || text.trim().isEmpty()) {
			isPasswordEmpty = true;
			return "";
		}
		return text.trim();
	}
	
	/**
	 * Validates the form, if there are errors they are being put in the errors map.
	 */
	public void validate() {
		errors.clear();
		
		if(nick.isEmpty()) {
			errors.put("nick", "Nickname is mandatory!");
		}
		
		if(firstName.isEmpty()) {
			errors.put("fn", "First name is mandatory!");
		}
		
		if(lastName.isEmpty()) {
			errors.put("ln", "Last name is mandatory!");
		}
		
		if(email.isEmpty()) {
			errors.put("email", "E-mail is mandatory!");
		} else {
			int l = email.length();
			int p = email.indexOf('@');
			if(l < 3 || p == -1 || p == 0 || p == l - 1) {
				errors.put("email", "E-mail address has invalid format!");
			}
		}
		
		if(isPasswordEmpty) {
			errors.put("password", "Password field is mandatory and cannot contain spaces!");	
		}
	}
	
	/**
	 * Getter for user id.
	 * 
	 * @return user id
	 */
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
	
}
