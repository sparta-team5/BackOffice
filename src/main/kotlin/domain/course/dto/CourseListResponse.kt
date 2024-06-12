package domain.course.dto

import domain.course.model.Category
import domain.course.model.Course

data class CourseListResponse(
    val title: String,
    val tutor: String,
    val description: String,
    val imageUrl: String,
    val category: Category,
    val isBookmarked: Boolean = false,
    val isSubscribed: Boolean = false,
    val viewCount: Long,
    val rate: Int?
) {
    companion object {
        fun from(course: Course) = CourseListResponse(
            title = course.title,
            tutor = course.tutor.name,
            category = course.category,
            description = course.description,
            imageUrl = course.imageUrl,
            viewCount = course.viewCount,
            rate = course.rate
        )
    }
}
