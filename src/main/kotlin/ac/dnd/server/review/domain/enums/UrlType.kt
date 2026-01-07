package ac.dnd.server.review.domain.enums

enum class UrlType(
    val description: String
) {
    LINKEDIN("링크드인"),
    GITHUB("깃허브"),
    EMAIL("이메일"),
    TISTORY("티스토리"),
    VELOG("벨로그"),
    MEDIUM("미디엄"),
    BEHANCE("비헨스"),
    INSTAGRAM("인스타그램"),
    YOUTUBE("유튜브")
}