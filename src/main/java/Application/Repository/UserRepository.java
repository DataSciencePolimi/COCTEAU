package Application.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import Application.Domain.User;

/**
 * An interface to manage the data access to the {@link Application.Domain.User} table.
 * 
 * @author	Corti Lorenzo
 * @author  Tocchetti Andrea
 * @since	1.0
 * 
 */

public interface UserRepository extends JpaRepository<User, Integer>{

	/**
	 * Returns the count of the {@link Application.Domain.User}(s) with the provided unique identifiers.
	 * 
	 * @param cookieValue	The unique identifiers of the {@link Application.Domain.User}.
	 * @return	The count of the {@link Application.Domain.User}(s) with the provided unique identifiers.
	 */
	@Query(value = "SELECT COUNT(*) FROM user WHERE cookie = :cookieValue", nativeQuery = true)
	int findAllWithCookie(@Param("cookieValue") String cookieValue);
	
	/**
	 * Returns the {@link Application.Domain.User} with the provided unique identifier.
	 * 
	 * @param cookieValue	The unique identifiers of the {@link Application.Domain.User}.
	 * @return If found, the {@link Application.Domain.User} with the provided unique identifier, null otherwise.
	 */
	@Query(value = "SELECT * FROM user WHERE cookie = :cookieValue", nativeQuery = true)
	Optional<User> findByCookie(@Param("cookieValue") String cookieValue);

	/**
	 * Returns the {@link Application.Domain.User} with the provided username.
	 * 
	 * @param username	The username of the {@link Application.Domain.User}.
	 * @return If found, the {@link Application.Domain.User} with the provided username, null otherwise.
	 */
	@Query(value = "SELECT * FROM user WHERE username = :username", nativeQuery = true)
	Optional<User> findByUsername(@Param("username") String username);
	
	/**
	 * Returns the last entry number of the {@link Application.Domain.User}(s).
	 * 
	 * @return If found, the last entry number of the {@link Application.Domain.User}(s), null otherwise.
	 */
	@Query(value = "SELECT MAX(entry_number) FROM user", nativeQuery = true)
	Optional<Integer> findMaxEntry();
	
	/**
	 * Returns the {@link Application.Domain.User} with the provided entry number.
	 * 
	 * @param userEntry	The entry number of the {@link Application.Domain.User}.
	 * @return	The {@link Application.Domain.User} with the provided entry number.
	 */
	@Query(value = "SELECT * FROM user WHERE entry_number = :userEntry", nativeQuery = true)
	Optional<User> findByUserEntryNumber(@Param("userEntry") int userEntry);
}
