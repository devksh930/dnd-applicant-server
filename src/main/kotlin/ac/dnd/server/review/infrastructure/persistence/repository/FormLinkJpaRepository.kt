package ac.dnd.server.review.infrastructure.persistence.repository

import ac.dnd.server.review.infrastructure.persistence.entity.FormLink
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import java.util.UUID

interface FormLinkJpaRepository : JpaRepository<FormLink, Long> {

    fun findByKey(key: UUID): FormLink?
}
