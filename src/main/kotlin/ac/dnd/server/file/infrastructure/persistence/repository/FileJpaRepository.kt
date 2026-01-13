package ac.dnd.server.file.infrastructure.persistence.repository

import ac.dnd.server.file.infrastructure.persistence.entity.File
import org.springframework.data.jpa.repository.JpaRepository

interface FileJpaRepository : JpaRepository<File, Long>
