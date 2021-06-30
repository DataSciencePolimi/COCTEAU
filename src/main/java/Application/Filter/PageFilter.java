package Application.Filter;

import java.io.IOException;
import java.util.Properties;
import java.util.regex.Pattern;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.SessionAttributes;

import Application.Utils.ReadPropertyFiles;

/**
 * Implementation of the {@link javax.servlet.Filter} interface that checks that the navigations paths of the application are respected.
 * 
 * @author	Corti Lorenzo
 * @author  Tocchetti Andrea
 * @since	1.0
 * 
 */

@SessionAttributes("previousPage")
@WebFilter("/*")
@Order(2)
public class PageFilter implements Filter {
	
	/**
	 * Private instance of the {@link Application.Utils.ReadPropertyFiles}.
	 * Used to read the navigation properties of the application.
	 */
	private ReadPropertyFiles readPropertyFiles;
	
	/**
	 * Private instance for accessing and working with the navigation properties
	 * of the application.
	 */
	private Properties navigationProperties = null;
	
	/**
	 * Constructor for the {@link Application.Filter.PageFilter} class.
	 * @param readPropertyFiles autowired, used to read the navigation properties of the application.
	 */
	@Autowired
	public PageFilter(ReadPropertyFiles readPropertyFiles) {
		this.readPropertyFiles = readPropertyFiles;
	}
	
	/**
	 * Implementation of the {@link javax.servlet.Filter#doFilter} method from {@link javax.servlet.Filter} class.
	 * Uses {@link Application.Filter.PageFilter#navigationProperties} to check for the correct navigation paths in
	 * the application.
	 * 
	 * For general resources, like folders with images, the requests are valid for all pages. 
	 * Other requests, like page-specific methods, are bound and validated with the respective page.
	 */
	@Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        
		HttpSession session = ((HttpServletRequest)servletRequest).getSession();
		String requestUrl = ((HttpServletRequest)servletRequest).getRequestURL().toString();
		Object previousPage = session.getAttribute("previousPage");
		
		if(navigationProperties == null) {
			session.setAttribute("previousPage", null);
			navigationProperties = readPropertyFiles.readPropFile("navigation.properties");
		}
		
		if(requestUrl.charAt(requestUrl.length() - 1) == '/' || requestUrl.contains("/access-denied") || requestUrl.contains("/error") || requestUrl.contains("/webjars/") || requestUrl.contains("/css/") || requestUrl.contains("/appimg/") || requestUrl.contains("/images/") || requestUrl.contains("/video/") || requestUrl.contains("/gdpr/") || requestUrl.contains("/imagesScenario/") || requestUrl.contains("/js/") || requestUrl.contains("/fontawesome-free/") || requestUrl.contains("favicon.ico") || requestUrl.contains("/profilePictures/") || requestUrl.contains("/imagesAchievement/")) {
			if(requestUrl.contains("/access-denied") || requestUrl.contains("/error")) {
				session.setAttribute("previousPage", "error");
			}
			
			if(requestUrl.charAt(requestUrl.length() - 1) == '/') {
				session.setAttribute("previousPage", "index");
			}
			filterChain.doFilter(servletRequest, servletResponse);
		} else {
			
			String navigation = null;
			String[] availableNext = null;
			
			if(previousPage != null && !previousPage.equals("error")) {
				navigation = navigationProperties.getProperty(previousPage.toString() + ".next");
				
				if(navigation != null) {
					navigation = navigation.replace(" ", "");
					availableNext = navigation.split(Pattern.quote(","));
				}
			}
			
			if(previousPage == null || requestUrl.contains("/saveUpdateCookieConsent") || requestUrl.contains("/getJoinButtons")) {
				session.setAttribute("previousPage", "index");
			} else if(requestUrl.contains("/scenario-panel") || requestUrl.contains("/getScenarioDetails") || requestUrl.contains("/editScenario") || requestUrl.contains("/hideScenario") || requestUrl.contains("/publishScenario") || requestUrl.contains("/deleteScenario")) {
				session.setAttribute("previousPage", "scenario-panel");
			} else if(requestUrl.contains("/scenario-edit") || requestUrl.contains("/updateScenario")) {
				session.setAttribute("previousPage", "scenario-edit");
			} else if(requestUrl.contains("/scenario-management") || requestUrl.contains("/retrieve-coupable-scenarios") || requestUrl.contains("/create-narrative") || requestUrl.contains("/create-scenario")) {
				session.setAttribute("previousPage", "scenario-management");
			} else if(requestUrl.contains("/scenario-deleted") || requestUrl.contains("/getDeletedScenarioDetails")) {
				session.setAttribute("previousPage", "scenario-deleted");
			} else if(requestUrl.contains("/vision-management") || requestUrl.contains("/get-unsplash-pics-management") || requestUrl.contains("/save-vision-management")) {
				session.setAttribute("previousPage", "vision-management");
			} else if(requestUrl.contains("/quiz-management") || requestUrl.contains("/submit-questions")) {
				session.setAttribute("previousPage", "quiz-management");
			} else if (requestUrl.contains("/login") || requestUrl.contains("/doLogin")) {
				session.setAttribute("previousPage", "login");
			} else if(requestUrl.contains("/scenario")) {
				session.setAttribute("previousPage", "scenario");
			} else if(requestUrl.contains("/quiz") || requestUrl.contains("/answerQuestion")) {
				session.setAttribute("previousPage", "quiz");
			} else if(requestUrl.contains("/feelings") || requestUrl.contains("/saveFeelings")) {
				session.setAttribute("previousPage", "feelings");
			} else if(requestUrl.contains("/userinfo") || requestUrl.contains("/saveInfo") || requestUrl.contains("/alwaysSkip")) {
				session.setAttribute("previousPage", "userinfo");
			} else if(requestUrl.contains("/feed") || requestUrl.contains("/feed-fragment")) {
				session.setAttribute("previousPage", "feed");
			} else if(requestUrl.contains("/vision") || requestUrl.contains("/saveVision") || requestUrl.contains("/getUnsplashPics")) {
				session.setAttribute("previousPage", "vision");
			} else if(requestUrl.contains("/match") || requestUrl.contains("/changePage") ||
					requestUrl.contains("/saveGuesses") || requestUrl.contains("/updated-position")) {
				session.setAttribute("previousPage", "match");
			} else if(requestUrl.contains("/in-depth") || requestUrl.contains("/saveThoughts")) {
				session.setAttribute("previousPage", "in-depth");
			} else if(requestUrl.contains("/about")) {
				session.setAttribute("previousPage", "about");
			} else if(requestUrl.contains("/statistics") || requestUrl.contains("/statisticsStat")) {
				session.setAttribute("previousPage", "statistics");
			} else if(requestUrl.contains("/profile") || requestUrl.contains("/updateUsername") || requestUrl.contains("/createProfile") || requestUrl.contains("/updateProfilePicture") || requestUrl.contains("/updatePassword")) {
				session.setAttribute("previousPage", "profile");
			} else if(requestUrl.contains("/achievement")) {
				session.setAttribute("previousPage", "achievement");
			} else if(requestUrl.contains("/create-admin") || requestUrl.contains("/createAdminProfile")) {
				session.setAttribute("previousPage", "create-admin");
			}
			
			boolean found = false;
			
			if(availableNext != null) {
				for(int i = 0; i < availableNext.length; i++) {
					if(requestUrl.contains(availableNext[i])) {
						found = true;
						break;
					}
				}
			}
			
			if(previousPage != null && previousPage.equals("error")) {
				((HttpServletResponse) servletResponse).sendRedirect("/");
			} else if(found || requestUrl.charAt(requestUrl.length() - 1) == '/') {
				filterChain.doFilter(servletRequest, servletResponse);
			} else {
				session.setAttribute("previousPage", "error");
				((HttpServletResponse) servletResponse).sendRedirect("/access-denied");
			}
		}
    }

}
