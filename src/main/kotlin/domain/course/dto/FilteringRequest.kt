package domain.course.dto

data class FilteringRequest(
    val title: String,
    val tutorName: String,
    val category: String,
    val rate: Double,
    val isBookmarked: Boolean,
    val isSubscribed: Boolean
)
