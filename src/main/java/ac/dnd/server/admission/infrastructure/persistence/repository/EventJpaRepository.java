package ac.dnd.server.admission.infrastructure.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import ac.dnd.server.admission.infrastructure.persistence.entity.Event;

public interface EventJpaRepository extends JpaRepository<Event, Long> {
}