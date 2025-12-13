package ac.dnd.server.review.infrastructure.persistence.repository

import ac.dnd.server.review.infrastructure.persistence.entity.Project
import org.springframework.data.jpa.repository.JpaRepository

interface ProjectJpaRepository : JpaRepository<Project, Long>
