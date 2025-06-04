package ac.dnd.server.admission.infrastructure.persistence.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import ac.dnd.server.admission.infrastructure.persistence.entity.Event;
import ac.dnd.server.common.support.EventStatus;

public interface EventJpaRepository extends JpaRepository<Event, Long> {

	List<Event> findByStatusIn(List<EventStatus> statuses);

}