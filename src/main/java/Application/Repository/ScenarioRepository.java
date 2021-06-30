package Application.Repository;

import java.util.Collection;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import Application.Domain.Quiz;
import Application.Domain.Scenario;

/**
 * An interface to manage the data access to the {@link Application.Domain.Scenario} table.
 * 
 * @author	Corti Lorenzo
 * @author  Tocchetti Andrea
 * @since	1.0
 * 
 */

public interface ScenarioRepository extends JpaRepository<Scenario, Integer>{

	/**
	 * Returns the list of {@link Application.Domain.Scenario}(s) with the provided language.
	 * 
	 * @param lang	The language of the {@link Application.Domain.Scenario}.
	 * @return	The list of {@link Application.Domain.Scenario}(s) with the provided language.
	 */
	@Query(value = "SELECT * FROM scenario WHERE lang = :lang AND deleted = 0", nativeQuery = true)
	Collection<Scenario> findByLanguage(@Param("lang") String lang);
	
	/**
	 * Returns the list of {@link Application.Domain.Scenario}(s) the {@link Application.Domain.User} is allowed to visualize.
	 * 
	 * @param lang	The language associated with the {@link Application.Domain.Scenario}.
	 * @param id_user	The unique identifier of the {@link Application.Domain.User}.
	 * @param refCode	The referral code associated with the {@link Application.Domain.Scenario}.
	 * @return	 The list of {@link Application.Domain.Scenario}(s) the {@link Application.Domain.User} is allowed to visualize.
	 */
	@Query(value = "SELECT s.* FROM scenario as s JOIN referral_code_scenario as rcs on s.id_scenario = rcs.scenario WHERE s.lang = :lang AND rcs.released = 1 AND s.deleted = 0 AND rcs.referral_code IN (SELECT referral_code FROM referral_code as rc JOIN user as u ON rc.id_referral_code = u.referral_code WHERE cookie = :id_user AND rc.value = :refCode) UNION SELECT s1.* FROM scenario as s1 JOIN referral_code_scenario as rcs1 on s1.id_scenario = rcs1.scenario WHERE s1.lang = :lang AND s1.deleted = 0 AND rcs1.released = 1 AND rcs1.referral_code IN (SELECT id_referral_code FROM referral_code as rc WHERE rc.value = \"universal\")", nativeQuery = true)
	Collection<Scenario> findByLanguageAndUser(@Param("lang") String lang, @Param("id_user") String id_user, @Param("refCode") String refCode);

	/**
	 * Returns the last group number among the {@link Application.Domain.Scenario}(s).
	 * 
	 * @return	The last group number among the {@link Application.Domain.Scenario}(s).
	 */
	@Query(value = "SELECT scenario_group FROM scenario ORDER BY scenario_group DESC LIMIT 1", nativeQuery = true)
	int findLastScenarioGroup();

	/**
	 * Returns the {@link Application.Domain.Quiz} associated with the {@link Application.Domain.Scenario} with the provided group number.
	 * 
	 * @param scenarioGroup	The number representing the group of the {@link Application.Domain.Scenario}.
	 * @return If found,  the {@link Application.Domain.Quiz} associated with the {@link Application.Domain.Scenario} with the provided group number, null otherwise.
	 */
	@Query(value = "SELECT quiz FROM scenario WHERE scenario_group = :scenarioGroup", nativeQuery = true)
	Optional<Quiz> findByScenarioGroup(@Param("scenarioGroup") int scenarioGroup);
	
	/**
	 * Returns the list of {@link Application.Domain.Scenario}(s) for which there is not an associated {@link Application.Domain.Scenario} in another language.
	 * 
	 * @param narrativeName	The name of the {@link Application.Domain.Narrative}
	 * @param narrativeLang	The language of the {@link Application.Domain.Narrative}
	 * @return	The list of {@link Application.Domain.Scenario}(s) for which there is not an associated {@link Application.Domain.Scenario} in another language.
	 */
	@Query(value = "SELECT * FROM scenario WHERE scenario_group NOT IN (SELECT scenario_group FROM scenario GROUP BY scenario_group HAVING count(*) > 1) AND id_narrative IN (SELECT id_narrative FROM narrative WHERE name = :narrativeName AND lang != :narrativeLang)", nativeQuery = true)
	Collection<Scenario> findAllWithoutScenarioGroup(@Param("narrativeName") String narrativeName, @Param("narrativeLang") String narrativeLang);
	
	/**
	 * Returns the last {@link Application.Domain.Scenario}.
	 * 
	 * @return	the last {@link Application.Domain.Scenario}.
	 */
	@Query(value = "SELECT * FROM scenario ORDER BY id_scenario DESC LIMIT 1", nativeQuery = true)
	Scenario findLastScenario();
	
	/**
	 * Returns the list of {@link Application.Domain.Scenario}(s) created by the {@link Application.Domain.User} with the provided unique identifier.
	 * 
	 * @param creatorId	The unique identifier of the {@link Application.Domain.User} who created the {@link Application.Domain.Scenario}(s).
	 * @return	The list of {@link Application.Domain.Scenario}(s) created by the {@link Application.Domain.User} with the provided unique identifier.
	 */
	@Query(value = "SELECT * FROM scenario WHERE creator = :creator AND deleted = 0", nativeQuery = true)
	Collection<Scenario> findByCreator(@Param("creator") String creatorId);
	
	/**
	 * Returns the list of the deleted {@link Application.Domain.Scenario}(s).
	 * 
	 * @return	The list of the deleted {@link Application.Domain.Scenario}(s).
	 */
	@Query(value = "SELECT * FROM scenario WHERE deleted = 1", nativeQuery = true)
	Collection<Scenario> findDeletedScenarios();
	
	/**
	 * Returns the deleted {@link Application.Domain.Scenario} with the provided unique identifier.
	 * 
	 * @param deletedScenarioId	The unique identifier of the {@link Application.Domain.Scenario}.
	 * @return	If found, the deleted {@link Application.Domain.Scenario} with the provided unique identifier, null otherwise.
	 */
	@Query(value = "SELECT * FROM scenario WHERE id_scenario = :id AND deleted = 1", nativeQuery = true)
	Optional<Scenario> findDeletedById(@Param("id") int deletedScenarioId);
}
