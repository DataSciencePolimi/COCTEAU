package Application.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import Application.Domain.ReferralCodeScenario;

/**
 * An interface to manage the data access to the {@link Application.Domain.ReferralCodeScenario} table.
 * 
 * @author	Corti Lorenzo
 * @author  Tocchetti Andrea
 * @since	1.0
 * 
 */

public interface ReferralCodeScenarioRepository extends JpaRepository<ReferralCodeScenario, Integer> {

}
