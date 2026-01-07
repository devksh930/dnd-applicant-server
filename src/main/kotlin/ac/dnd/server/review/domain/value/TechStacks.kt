package ac.dnd.server.review.domain.value

data class TechStacks(
    val values: List<String> = emptyList()
) {
    companion object {
        fun of(values: List<String>?): TechStacks {
            return TechStacks(values ?: emptyList())
        }
    }
}
