package ac.dnd.server.admission.infrastructure.persistence.entity

import ac.dnd.server.admission.infrastructure.persistence.converter.StringCryptoConverter
import ac.dnd.server.admission.infrastructure.persistence.converter.StringHmacConverter
import ac.dnd.server.global.base.BaseEntity
import ac.dnd.server.enums.ApplicantStatus
import ac.dnd.server.enums.ApplicantType
import jakarta.persistence.*

@Entity
@Table(name = "applicant")
class Applicant(
    @Convert(converter = StringCryptoConverter::class)
    val name: String,

    @Convert(converter = StringCryptoConverter::class)
    val email: String,

    @Convert(converter = StringHmacConverter::class)
    val nameBlindIndex: String,

    @Convert(converter = StringHmacConverter::class)
    val emailBlindIndex: String,

    @Enumerated(EnumType.STRING)
    val type: ApplicantType,

    @Enumerated(EnumType.STRING)
    val status: ApplicantStatus
) : BaseEntity() {

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id")
    var event: Event? = null
        private set

    protected constructor() : this("", "", "", "", ApplicantType.FRONTEND, ApplicantStatus.NONE)

    fun withEvent(event: Event) {
        this.event = event
    }
}
