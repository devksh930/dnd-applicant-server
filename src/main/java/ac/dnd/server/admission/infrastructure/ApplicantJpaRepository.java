package ac.dnd.server.admission.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;

import ac.dnd.server.admission.domain.Applicant;

public interface ApplicantJpaRepository extends JpaRepository<Applicant, Long> {
}