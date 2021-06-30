package Application.Domain;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * Credentials is the class that maps the database Entity Credentials.
 * The Credentials represent the set of attributes useful to understand whether the {@link Application.Domain.User} is able to login in the system or not.
 * 
 * @author	Corti Lorenzo
 * @author  Tocchetti Andrea
 * @since	1.0
 * 
 */

@MappedSuperclass
public class Credentials {
	
	/**
	 * The username of the {@link Application.Domain.User}.
	 */
	@NotEmpty
	protected String username;
	
	/**
	 * The password of the {@link Application.Domain.User}.
	 */
	@NotEmpty
	protected String password;
	
	/**
	 * Whether the {@link Application.Domain.User} accepted the cookies or not.
	 */
	@NotNull
	@Column(name = "cookie_consent")
	protected boolean cookieConsent;
	
	/**
	 * Returns the username of the {@link Application.Domain.User}.
	 * 
	 * @return	The username of the {@link Application.Domain.User}.
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * Sets the username of the {@link Application.Domain.User}.
	 * 
	 * @param username	The username of the {@link Application.Domain.User}.
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * Returns the password of the {@link Application.Domain.User}.
	 * 
	 * @return	The password of the {@link Application.Domain.User}.
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * Sets the password of the {@link Application.Domain.User}.
	 * 
	 * @param password	The password of the {@link Application.Domain.User}.
	 */
	public void setPassword(String password) {
		this.password = password;
	}
	
	/**
	 * Returns whether the {@link Application.Domain.User} accepted the cookies or not.
	 * 
	 * @return	Whether the {@link Application.Domain.User} accepted the cookies or not.
	 */
	public boolean isCookieConsent() {
		return cookieConsent;
	}

	/**
	 * Sets whether the {@link Application.Domain.User} accepted the cookies or not.
	 * 
	 * @param cookieConsent	Whether the {@link Application.Domain.User} accepted the cookies or not.
	 */
	public void setCookieConsent(boolean cookieConsent) {
		this.cookieConsent = cookieConsent;
	}

}
