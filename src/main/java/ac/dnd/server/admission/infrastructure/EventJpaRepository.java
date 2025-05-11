package ac.dnd.server.admission.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;

import ac.dnd.server.admission.domain.Event;

public interface EventJpaRepository extends JpaRepository<Event, Long> {
}