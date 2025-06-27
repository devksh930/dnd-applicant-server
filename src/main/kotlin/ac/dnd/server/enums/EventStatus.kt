package ac.dnd.server.enums

enum class EventStatus(val description: String) {
    PENDING("진행중"),
    COMPLETED("완료"),
    EXPIRED("만료");

    fun isPublished(): Boolean {
        return this == COMPLETED
    }

    fun isPending(): Boolean {
        return this == PENDING
    }
}