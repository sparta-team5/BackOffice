package team5.backoffice.domain.course.dto

import team5.backoffice.domain.course.model.Category

data class FilteringRequest(
    val title: String,
    val description: String,
    val tutorNickName: String,
//    val category: Category,
//    val rate: Double,
//    val isBookmarked: Boolean,
//    val isSubscribed: Boolean
)
