package ac.dnd.server.shared.persistence

import jakarta.persistence.*
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import java.time.LocalDateTime

@MappedSuperclass
abstract class BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null

    @CreationTimestamp
    @Column
    val createdAt: LocalDateTime? = null

    @UpdateTimestamp
    @Column
    val updatedAt: LocalDateTime? = null
}
