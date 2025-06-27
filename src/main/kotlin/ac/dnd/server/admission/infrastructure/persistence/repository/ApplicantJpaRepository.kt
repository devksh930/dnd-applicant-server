package ac.dnd.server.admission.infrastructure.persistence.repository

import ac.dnd.server.admission.infrastructure.persistence.dto.ApplicantStatusDto
import ac.dnd.server.admission.infrastructure.persistence.entity.Applicant
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import java.util.*

interface ApplicantJpaRepository : JpaRepository<Applicant, Long> {

    @Query("""
        SELECT a
        FROM Applicant a
        JOIN FETCH a.event e
        WHERE e.id = :eventId
                AND a.nameBlindIndex = :name
                AND a.emailBlindIndex = :email
        """)
    fun findByEventIdAndNameAndEmail(
        eventId: Long,
        name: String,
        email: String
    ): Optional<Applicant>

    @Query("""
        SELECT new ac.dnd.server.admission.infrastructure.persistence.dto.ApplicantStatusDto(
            e.name, a.name, a.status
        )
        FROM Applicant a
        JOIN a.event e
        WHERE e.id = :eventId
                AND a.nameBlindIndex = :nameBlindIndex
                AND a.emailBlindIndex = :emailBlindIndex
        """)
    fun findApplicantStatusByEventIdAndNameAndEmail(
        eventId: Long,
        nameBlindIndex: String,
        emailBlindIndex: String
    ): Optional<ApplicantStatusDto>
}
