package Application.Repository;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import Application.Domain.Keyword;

/**
 * An interface to manage the data access to the {@link Application.Domain.Keyword} table.
 * 
 * @author	Corti Lorenzo
 * @author  Tocchetti Andrea
 * @since	1.0
 * 
 */

public interface KeywordRepository extends JpaRepository<Keyword, Integer>{
	
	/**
	 * Returns the texts of all the {@link Application.Domain.Keyword}(s).
	 * 
	 * @return	The texts of all the {@link Application.Domain.Keyword}(s).
	 */
	@Query(value = "SELECT keyword FROM keyword", nativeQuery = true)
	ArrayList<String> findAllString();

	/**
	 * Returns the {@link Application.Domain.Keyword} with the provided text.
	 * 
	 * @param keywordName	The text of the {@link Application.Domain.Keyword}.
	 * @return	the {@link Application.Domain.Keyword} with the provided text.
	 */
	@Query(value = "SELECT * FROM keyword WHERE keyword LIKE BINARY :keywordName", nativeQuery = true)
	Keyword findByName(@Param("keywordName") String keywordName);
	
	/**
	 * Returns the list of {@link Application.Domain.Keyword}s with the provided unique identifier.
	 * 
	 * @param id_vision	The unique identifier of the {@link Application.Domain.Vision}.
	 * @return	the list of {@link Application.Domain.Keyword}s with the provided unique identifier.
	 */
	@Query(value = "SELECT * FROM keyword as k JOIN vision_keyword as vk ON k.id_keyword = vk.id_keyword WHERE vk.id_vision = :id_vision", nativeQuery = true)
	Collection<Keyword> findByVision(@Param("id_vision") int id_vision);
}
