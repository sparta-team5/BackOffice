package domain.course.dto

import domain.course.model.Course

data class CourseListResponse(
    val id: Long,
    val title: String,
    val tutor: String,
    val description: String,
    val imageUrl: String,
    val category: String,
    val isBookmarked: Boolean = false,
    val isSubscribed: Boolean = false,
    val viewCount: Long
) {
    companion object {
        fun from(course: Course, isBookmarked: Boolean, isSubscribed: Boolean) = CourseListResponse(
            id = course.id!!,
            title = course.title,
            tutor = course.tutor.nickname,
            category = course.category,
            description = course.description,
            imageUrl = course.imageUrl,
            viewCount = course.viewCount,
            isBookmarked = isBookmarked,
            isSubscribed = isSubscribed
        )
    }
}
