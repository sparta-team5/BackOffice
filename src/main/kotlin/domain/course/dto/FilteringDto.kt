package domain.course.dto

data class FilteringDto(
    val title: String,
    val tutorName: String,
    val category: Category,
    val rate: Int,
    val isBookmarked: Boolean,
    val isSubscribed: Boolean
)
