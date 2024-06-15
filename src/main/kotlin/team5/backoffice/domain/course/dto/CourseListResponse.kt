package team5.backoffice.domain.course.dto

import java.time.LocalDateTime

data class CourseListResponse(
    val id: Long,
    val title: String,
    val tutor: String,
    val description: String,
    val imageUrl: String,
    val category: String,
    val isBookmarked: Boolean = false,
    val isSubscribed: Boolean = false,
    val viewCount: Long,
    val createdAt: LocalDateTime,
    val rate: Double
) {
    companion object {
        fun from(course: CourseLowData, isBookmarked: Boolean, isSubscribed: Boolean) = CourseListResponse(
            id = course.id,
            title = course.title,
            tutor = course.tutor.nickname,
            category = course.categoryName,
            description = course.description,
            imageUrl = course.imageUrl,
            viewCount = course.viewCount,
            isBookmarked = isBookmarked,
            isSubscribed = isSubscribed,
            createdAt = course.createdAt,
            rate = course.rate,
        )
    }
}
