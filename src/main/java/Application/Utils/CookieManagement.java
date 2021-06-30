package Application.Utils;

import javax.servlet.http.Cookie;

import org.apache.commons.lang3.RandomStringUtils;

import Application.Repository.UserRepository;

/**
 * Utility class used to create new {@link Application.Domain.User} cookie identifiers.
 * 
 * @author	Corti Lorenzo
 * @author  Tocchetti Andrea
 * @since	1.0
 * 
 */

public class CookieManagement {
	
	/**
	 * Checks for existing cookies and generates a new one.
	 * 
	 * @param userRepository instance of {@link Application.Repository.UserRepository}.
	 * @return {@link javax.servlet.http.Cookie} object containing the new cookie.
	 */
	public static Cookie createNewCookie(UserRepository userRepository) {
		String cookieValue = "";
		do {
			cookieValue = RandomStringUtils.randomAlphanumeric(20);
		} while(userRepository.findAllWithCookie(cookieValue) == 1);
		Cookie cookie = new Cookie("user_id", cookieValue);
		cookie.setMaxAge(60 * 60 * 24 * 365); //1 Year
		cookie.setPath("/"); //accesible everywhere
		return cookie;
	}
	
	/**
	 * Create a cookie given name and value. This function is mainly used in the login process.
	 * Once the credentials match, this function is called to create the cookie for the authenticated {@link Application.Domain.User}.
	 * 
	 * @param name String, name of the cookie.
	 * @param value String, value to assign to the cookie.
	 * @return {@link javax.servlet.http.Cookie} object containing the requested cookie.
	 */
	public static Cookie createCookie(String name, String value) {
		Cookie cookie = new Cookie(name, value);
		cookie.setMaxAge(60 * 60 * 24 * 365);
		cookie.setPath("/");
		return cookie;
	}
}
