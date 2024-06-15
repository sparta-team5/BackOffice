package team5.backoffice.domain.course.dto

import team5.backoffice.domain.course.model.Course
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
        fun from(course: Course, isBookmarked: Boolean, isSubscribed: Boolean, rate: Double) = CourseListResponse(
            id = course.id!!,
            title = course.title,
            tutor = course.tutor.nickname,
            category = course.category.name,
            description = course.description,
            imageUrl = course.imageUrl,
            viewCount = course.viewCount,
            isBookmarked = isBookmarked,
            isSubscribed = isSubscribed,
            createdAt = course.createdAt,
            rate = rate
        )
    }
}
