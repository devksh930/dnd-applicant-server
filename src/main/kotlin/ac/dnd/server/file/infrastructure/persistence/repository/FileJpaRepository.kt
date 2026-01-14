package ac.dnd.server.file.infrastructure.persistence.repository

import ac.dnd.server.file.infrastructure.persistence.entity.FileEntity
import org.springframework.data.jpa.repository.JpaRepository

interface FileJpaRepository : JpaRepository<FileEntity, Long>
