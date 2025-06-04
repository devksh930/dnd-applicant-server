package ac.dnd.server.admission.infrastructure.persistence.adapter;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import ac.dnd.server.admission.domain.AdmissionRepository;
import ac.dnd.server.admission.domain.model.ApplicantData;
import ac.dnd.server.admission.domain.model.EventData;
import ac.dnd.server.admission.domain.model.EventsData;
import ac.dnd.server.admission.infrastructure.persistence.crypto.HmacBlindIndexCreator;
import ac.dnd.server.admission.infrastructure.persistence.entity.Event;
import ac.dnd.server.admission.infrastructure.persistence.mapper.ApplicantPersistenceMapper;
import ac.dnd.server.admission.infrastructure.persistence.repository.ApplicantJpaRepository;
import ac.dnd.server.admission.infrastructure.persistence.repository.EventJpaRepository;
import ac.dnd.server.common.support.EventStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Repository
@RequiredArgsConstructor
public class AdmissionRepositoryAdapter implements AdmissionRepository {

	private final EventJpaRepository eventJpaRepository;
	private final ApplicantJpaRepository applicantJpaRepository;
	private final ApplicantPersistenceMapper applicantPersistenceMapper;
	private final HmacBlindIndexCreator blindIndexCreator;

	@Override
	public Long saveEvent(final EventData domain) {
		final Event event = applicantPersistenceMapper.eventDataToEntity(domain);
		return eventJpaRepository.save(event)
			.getId();
	}

	@Override
	public List<EventData> getEvents() {
		return eventJpaRepository.findAll().stream()
			.map(applicantPersistenceMapper::eventEntityToDomain)
			.toList();
	}

	@Override
	public Optional<ApplicantData> findAdmissionByEventIdAndNameAndEmail(
		final Long eventId,
		final String name,
		final String email
	) {
		log.info(
			"name: {}, email: {}",
			name,
			email
		);
		return applicantJpaRepository.findByEventIdAndNameAndEmail(
			eventId,
			blindIndexCreator.create(name),
			blindIndexCreator.create(email)
		).map(applicantPersistenceMapper::applicantEntityToDomain);

	}

	@Override
	public EventsData findByStatusIn(final List<EventStatus> statuses) {
		return new EventsData(eventJpaRepository.findByStatusIn(statuses)
			.stream()
			.map(applicantPersistenceMapper::eventEntityToDomain)
			.toList());
	}

}
