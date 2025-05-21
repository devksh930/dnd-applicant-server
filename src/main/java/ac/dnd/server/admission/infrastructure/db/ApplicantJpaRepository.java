package ac.dnd.server.admission.infrastructure.db;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import ac.dnd.server.admission.domain.Applicant;

public interface ApplicantJpaRepository extends JpaRepository<Applicant, Long> {

	@Query("""
		SELECT a 
		FROM Applicant a 
		JOIN FETCH a.event e
		WHERE e.id = :eventId 
				AND a.name = :name 
			    AND a.email = :email
		""")
	Optional<Applicant> findByEventIdAndNameAndEmail(
		final Long eventId,
		final String name,
		final String email
	);
}