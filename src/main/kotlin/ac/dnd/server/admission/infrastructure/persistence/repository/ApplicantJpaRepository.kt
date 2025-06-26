package ac.dnd.server.admission.infrastructure.persistence.repository

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
}