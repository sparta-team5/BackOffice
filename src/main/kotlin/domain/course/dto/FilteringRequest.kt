package domain.course.dto

data class FilteringRequest(
    val title: String,
    val tutorName: String,
    val category: Category,
    val rate: Int,
    val isBookmarked: Boolean,
    val isSubscribed: Boolean
)
