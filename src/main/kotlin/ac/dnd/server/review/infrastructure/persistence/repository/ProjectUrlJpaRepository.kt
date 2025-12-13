package ac.dnd.server.review.infrastructure.persistence.repository

import ac.dnd.server.review.infrastructure.persistence.entity.ProjectUrl
import org.springframework.data.jpa.repository.JpaRepository

interface ProjectUrlJpaRepository : JpaRepository<ProjectUrl, Long>
