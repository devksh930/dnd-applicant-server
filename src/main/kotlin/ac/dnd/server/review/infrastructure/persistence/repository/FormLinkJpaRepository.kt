package ac.dnd.server.review.infrastructure.persistence.repository

import ac.dnd.server.review.infrastructure.persistence.entity.FormLink
import org.springframework.data.jpa.repository.JpaRepository

interface FormLinkJpaRepository : JpaRepository<FormLink, Long>
