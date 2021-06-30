package Application.Repository;

import java.util.Collection;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import Application.Domain.Narrative;

/**
 * An interface to manage the data access to the {@link Application.Domain.Narrative} table.
 * 
 * @author	Corti Lorenzo
 * @author  Tocchetti Andrea
 * @since	1.0
 * 
 */

public interface NarrativeRepository extends JpaRepository<Narrative, Integer>{

	/**
	 * Returns the list of {@link Application.Domain.Narrative}(s) with the provided language.
	 * 
	 * @param lang	The language of the {@link Application.Domain.Narrative}.
	 * @return	The list of {@link Application.Domain.Narrative}(s) with the provided language.
	 */
	@Query(value = "SELECT * FROM narrative WHERE lang = :lang ORDER BY id_narrative DESC", nativeQuery = true)
	Collection<Narrative> findByLanguage(@Param("lang") String lang);
	
	/**
	 * Returns the {@link Application.Domain.Narrative} with the provided unique identifier.
	 * 
	 * @param id_narrative	The unique identifier of the {@link Application.Domain.Narrative}.
	 * @return	The {@link Application.Domain.Narrative} with the provided unique identifier.
	 */
	@Query(value = "SELECT * FROM narrative WHERE id_narrative = :id_narrative", nativeQuery = true)
	Optional<Narrative> findById(@Param("id_narrative") int id_narrative);
}
