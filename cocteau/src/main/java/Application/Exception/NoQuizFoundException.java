package Application.Exception;

/**
 * Exception thrown by {@link Application.Management.DeletedScenarioManagementController} and {@link Application.Management.ScenarioManagementController} 
 * when viewing information for a {@link Application.Domain.Scenario} for which a {@link Application.Domain.Quiz} is not present.
 * 
 * @author	Corti Lorenzo
 * @author  Tocchetti Andrea
 * @since	1.0
 * 
 */

public class NoQuizFoundException extends Exception {

	private static final long serialVersionUID = 1L;
	
	private final String message = "No Quiz found for this Scenario";

	public String getMessage() {
		return message;
	}
	
}
