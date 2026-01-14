package ac.dnd.server.review.infrastructure.persistence.repository

import ac.dnd.server.review.infrastructure.persistence.entity.ProjectEntity
import org.springframework.data.jpa.repository.JpaRepository

interface ProjectJpaRepository : JpaRepository<ProjectEntity, Long>
