package Application.Filter;

import java.io.IOException;
import java.util.Optional;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;

import Application.Domain.Administrator;
import Application.Domain.User;
import Application.Repository.AdministratorRepository;
import Application.Repository.UserRepository;

/**
 * Implementation of the {@link javax.servlet.Filter} interface that checks the cookies exchanged between front-end and back-end.
 * 
 * @author	Corti Lorenzo
 * @author  Tocchetti Andrea
 * @since	1.0
 * 
 */

@WebFilter("/*")
@Order(1)
public class CookieFilter implements Filter {

	/**
	 * Private instance of the {@link Application.Repository.UserRepository} class. Used to check a {@link Application.Domain.User}'s details.
	 */
	private final UserRepository userRepository;
	/**
	 * Private instance of the {@link Application.Repository.AdministratorRepository} class. Used to check a {@link Application.Domain.Administrator}'s details.
	 */
	private final AdministratorRepository administratorRepository;
	
	/**
	 * Constructor for the {@link Application.Filter.PageFilter} class.
	 * 
	 * @param userRepository autowired, used to access persistent data about a {@link Application.Domain.User}.
	 * @param administratorRepository autowired, used to access persistent data about an {@link Application.Domain.Administrator}.
	 */
	@Autowired
	public CookieFilter(UserRepository userRepository, AdministratorRepository administratorRepository) {
		this.userRepository = userRepository;
		this.administratorRepository = administratorRepository;
	}

	/**
	 * Implementation of the {@link javax.servlet.Filter#doFilter} method from {@link javax.servlet.Filter} class.
	 * Uses {@link Application.Filter.CookieFilter#userRepository} and {@link Application.Filter.CookieFilter#administratorRepository} 
	 * to access and check persistent data about {@link Application.Domain.User}(s) and {@link Application.Domain.Administrator}(s).
	 * 
	 * If the cookie <strong> user_id </strong> is passed it is checked to be valid, depending on the request.
	 * The same applies for the cookie <strong> cookieconsent_status </strong>. If set to 'deny' the session is invalidated.
	 */
	@Override
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
		Cookie[] cookies = ((HttpServletRequest) servletRequest).getCookies();
		
		String requestUrl = ((HttpServletRequest) servletRequest).getRequestURL().toString();
		
		if(requestUrl.contains("/about") || requestUrl.contains("/get-data") || requestUrl.contains("/login") || 
				requestUrl.contains("/saveUpdateCookieConsent") || requestUrl.contains("/getJoinButtons") || requestUrl.charAt(requestUrl.length() - 1) == '/' || 
				requestUrl.contains("/access-denied") || requestUrl.contains("/error") || requestUrl.contains("/webjars/") || requestUrl.contains("/css/") || 
				requestUrl.contains("/appimg/") || requestUrl.contains("/images/") || requestUrl.contains("/imagesScenario/") || requestUrl.contains("/video/") || 
				requestUrl.contains("/gdpr/") || requestUrl.contains("/js/") || requestUrl.contains("/fontawesome-free/") || requestUrl.contains("favicon.ico") || 
				requestUrl.contains("/profilePictures/") || requestUrl.contains("/imagesAchievement/")) {
			filterChain.doFilter(servletRequest, servletResponse);
		} else if(requestUrl.contains("/logout")) {
			Cookie invalidCookie = new Cookie("user_id", null);
			invalidCookie.setMaxAge(0);
			invalidCookie.setPath("/");
			((HttpServletResponse) servletResponse).addCookie(invalidCookie);
			
			HttpSession session = ((HttpServletRequest) servletRequest).getSession(false);
			
	        if(session != null) {
	            session.invalidate();
	        }
			
			((HttpServletResponse) servletResponse).sendRedirect("/");
			return;
		} else {
			if (cookies != null) {
				boolean found = false;
				Cookie userCookie = null;
				Cookie consentCookie = null;
				
				for (Cookie ck : cookies) {
					if(ck.getName().equals("user_id")) {
						userCookie = ck;
					}
					if(ck.getName().equals("cookieconsent_status")) {
						consentCookie = ck;
					}
				}
				
				if (userCookie != null && consentCookie != null) {
					Optional<User> tempUser = userRepository.findByCookie(userCookie.getValue());
					Optional<Administrator> tempAdmin = administratorRepository.findByCookie(userCookie.getValue());
					if(tempUser.isPresent() && tempUser.get().isCookieConsent() && consentCookie.getValue().equals("allow")) {
						found = true;
					} else if(tempAdmin.isPresent() && consentCookie.getValue().equals("allow")) {
						found = true;
					}
				} else if(userCookie == null && requestUrl.contains("/scenario") && consentCookie != null && consentCookie.getValue().equals("allow")){
					found = true;
				}
				
				if(!found) {
					Cookie invalidCookie = new Cookie("user_id", "");
					invalidCookie.setMaxAge(0);
					invalidCookie.setPath("/");
					((HttpServletResponse) servletResponse).addCookie(invalidCookie);
					
					Cookie invalidConsent = new Cookie("cookieconsent_status", "deny");
					invalidConsent.setMaxAge(0);
					invalidConsent.setPath("/");
					((HttpServletResponse) servletResponse).addCookie(invalidConsent);
					
					Cookie errorMsg = new Cookie("errorMsg", "1");
					errorMsg.setMaxAge(60);
					errorMsg.setPath("/");
					((HttpServletResponse) servletResponse).addCookie(errorMsg);
					((HttpServletResponse) servletResponse).sendRedirect("/");
					return;
				} else {
					filterChain.doFilter(servletRequest, servletResponse);
				}
			} else {
				filterChain.doFilter(servletRequest, servletResponse);
			}
		}
	}
}
