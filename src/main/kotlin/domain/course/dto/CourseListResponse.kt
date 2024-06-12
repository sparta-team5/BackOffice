package domain.course.dto

import domain.course.model.Course

data class CourseListResponse(
    val courseId: Long?,
    val title: String,
    val tutor: String,
    val description: String,
    val imageUrl: String,
    val category: String,
    val isBookmarked: Boolean = false,
    val isSubscribed: Boolean = false,
    val viewCount: Long,
    val rate: Double
) {
    companion object {
        fun from(course: Course, isBookmarked: Boolean, isSubscribed: Boolean) = CourseListResponse(
            courseId = course.id,
            title = course.title,
            tutor = course.tutor.name,
            category = course.category,
            description = course.description,
            imageUrl = course.imageUrl,
            viewCount = course.viewCount,
            rate = course.rate,
            isBookmarked = isBookmarked,
            isSubscribed = isSubscribed
        )
    }
}
