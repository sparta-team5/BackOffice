package domain.course.dto

import domain.course.model.Category
import domain.course.model.Course

data class CourseSimpleResponse(
    val title: String,
    val description: String,
    val imageUrl: String,
    val category: Category,
    val rate: Int?,
) {
    companion object {
        fun from(course: Course) = CourseSimpleResponse(
            title = course.title,
            description = course.description,
            category = course.category,
            imageUrl = course.imageUrl,
            rate = course.rate
        )
    }
}
