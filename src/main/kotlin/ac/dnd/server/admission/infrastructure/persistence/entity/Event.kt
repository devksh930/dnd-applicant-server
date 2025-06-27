package ac.dnd.server.admission.infrastructure.persistence.entity

import ac.dnd.server.admission.domain.model.ViewablePeriod
import ac.dnd.server.global.base.BaseEntity
import ac.dnd.server.enums.EventStatus
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "event")
class Event(
    val name: String,

    @Embedded
    val period: ViewablePeriod,

    val resultAnnouncementDateTime: LocalDateTime,

    @Enumerated(EnumType.STRING)
    val status: EventStatus
) : BaseEntity() {

    protected constructor() : this(
        "",
        ViewablePeriod.of(LocalDateTime.now(), LocalDateTime.now()),
        LocalDateTime.now(),
        EventStatus.PENDING
    )
}
