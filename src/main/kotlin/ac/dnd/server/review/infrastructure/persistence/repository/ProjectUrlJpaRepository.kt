package ac.dnd.server.review.infrastructure.persistence.repository

import ac.dnd.server.review.infrastructure.persistence.entity.ProjectUrlEntity
import org.springframework.data.jpa.repository.JpaRepository

interface ProjectUrlJpaRepository : JpaRepository<ProjectUrlEntity, Long> {
    fun deleteByProjectId(projectId: Long)
}
