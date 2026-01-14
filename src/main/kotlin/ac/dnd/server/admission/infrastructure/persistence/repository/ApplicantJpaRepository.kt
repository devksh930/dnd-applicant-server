package ac.dnd.server.admission.infrastructure.persistence.repository

import ac.dnd.server.admission.domain.model.ApplicantStatusData
import ac.dnd.server.admission.infrastructure.persistence.entity.ApplicantEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface ApplicantJpaRepository : JpaRepository<ApplicantEntity, Long> {

    @Query(
        """
        SELECT a
        FROM ApplicantEntity a
        JOIN FETCH a.event e
        WHERE e.id = :eventId
                AND a.nameBlindIndex = :name
                AND a.emailBlindIndex = :email
        """
    )
    fun findByEventIdAndNameAndEmail(
        eventId: Long,
        name: String,
        email: String
    ): ApplicantEntity?

    @Query(
        """
        SELECT new ac.dnd.server.admission.domain.model.ApplicantStatusData(
            e.name, a.name, a.status
        )
        FROM ApplicantEntity a
        JOIN a.event e
        WHERE e.id = :eventId
                AND a.nameBlindIndex = :nameBlindIndex
                AND a.emailBlindIndex = :emailBlindIndex
        """
    )
    fun findApplicantStatusByEventIdAndNameAndEmail(
        eventId: Long,
        nameBlindIndex: String,
        emailBlindIndex: String
    ): ApplicantStatusData?
}
