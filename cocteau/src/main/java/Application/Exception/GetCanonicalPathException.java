package Application.Exception;

/**
 * Exception thrown by {@link Application.Application} when trying to register locations for {@link Application.Application#IMAGE_DIR}, 
 * {@link Application.Application#IMAGE_SCEN_DIR}, {@link Application.Application#LOG_DIR}, {@link Application.Application#PROF_DIR}
 * or {@link Application.Application#ACH_DIR}.
 * 
 * @author	Corti Lorenzo
 * @author  Tocchetti Andrea
 * @since	1.0
 * 
 */

public class GetCanonicalPathException extends Exception {

	private static final long serialVersionUID = 1L;
	
}
