package Application.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import Application.Domain.ReferralCode;

/**
 * An interface to manage the data access to the {@link Application.Domain.ReferralCode} table.
 * 
 * @author	Corti Lorenzo
 * @author  Tocchetti Andrea
 * @since	1.0
 * 
 */

public interface ReferralCodeRepository extends JpaRepository<ReferralCode, Integer>  {

	/**
	 * Returns the {@link Application.Domain.ReferralCode} provided their value.
	 * 
	 * @param referral	The value of the {@link Application.Domain.ReferralCode}.
	 * @return	The {@link Application.Domain.ReferralCode} provided their value
	 */
	@Query(value = "SELECT * FROM referral_code WHERE value = :referral", nativeQuery = true)
	Optional<ReferralCode> findByReferral(@Param("referral") String referral);
	
}
