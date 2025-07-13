package ac.dnd.server.admission.domain.model

data class ApplicantNameEmailBlindIndex(
    val nameBlindIndex: String,
    val emailBlindIndex: String
) {
    init {
        require(nameBlindIndex.isNotBlank()) {
            "이름 블라인드 인덱스(nameBlindIndex)는 공백일 수 없습니다."
        }
        require(emailBlindIndex.isNotBlank()) {
            "이메일 블라인드 인덱스(emailBlindIndex)는 공백일 수 없습니다."
        }
    }

    companion object {
        fun of(nameBlindIndex: String, emailBlindIndex: String): ApplicantNameEmailBlindIndex {
            return ApplicantNameEmailBlindIndex(nameBlindIndex, emailBlindIndex)
        }
    }
}