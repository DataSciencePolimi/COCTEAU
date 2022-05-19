package Application.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import Application.Domain.Administrator;

/**
 * An interface to manage the data access to the {@link Application.Domain.Administrator} table.
 * 
 * @author	Corti Lorenzo
 * @author  Tocchetti Andrea
 * @since	1.0
 * 
 */

public interface AdministratorRepository extends JpaRepository<Administrator, Integer>{

	/**
	 * Returns the {@link Application.Domain.Administrator} given their username.
	 * 
	 * @param username	The username of the {@link Application.Domain.Administrator}.
	 * @return	If found, the {@link Application.Domain.Administrator} with provided username, null otherwise.
	 */
	@Query(value = "SELECT * FROM administrator WHERE username = :username", nativeQuery = true)
	Optional<Administrator> findByUsername(@Param("username") String username);
	
	/**
	 * Returns the {@link Application.Domain.Administrator} given their unique identifier.
	 * 
	 * @param cookie	The unique identifier associated with the {@link Application.Domain.Administrator}.
	 * @return	If found, the {@link Application.Domain.Administrator} with the provided unique identifier, null otherwise.
	 */
	@Query(value = "SELECT * FROM administrator WHERE id_administrator = :cookie", nativeQuery = true)
	Optional<Administrator> findByCookie(@Param("cookie") String cookie);
	
	/**
	 * Counts the {@link Application.Domain.Administrator}(s) given an unique identifier.
	 * 
	 * @param cookieValue	The unique identifier associated with the {@link Application.Domain.Administrator}.
	 * @return The count of all the {@link Application.Domain.Administrator} with the provided unique identifier.
	 */
	@Query(value = "SELECT COUNT(*) FROM administrator WHERE id_administrator = :cookieValue", nativeQuery = true)
	int findAllWithCookie(@Param("cookieValue") String cookieValue);
}
