package ac.dnd.server.review.infrastructure.persistence.repository

import ac.dnd.server.review.infrastructure.persistence.entity.FormLinkEntity
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface FormLinkJpaRepository : JpaRepository<FormLinkEntity, Long> {
    fun findByKey(key: UUID): FormLinkEntity?
}
