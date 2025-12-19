package ac.dnd.server.file.infrastructure.persistence.entity

import ac.dnd.server.shared.persistence.BaseEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Table

@Entity
@Table(name = "files")
class File(
    @Column(nullable = false)
    val originalFileName: String,

    @Column(nullable = false)
    val storedFileName: String,

    @Column(nullable = false)
    val fileUrl: String,

    @Column(nullable = false)
    val fileSize: Long,

    @Column(nullable = false)
    val contentType: String
) : BaseEntity()
