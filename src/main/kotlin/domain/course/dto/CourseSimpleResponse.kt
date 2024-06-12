package domain.course.dto

import domain.course.model.Course

data class CourseSimpleResponse(
    val courseId: Long?,
    val title: String,
    val description: String,
    val imageUrl: String,
    val category: String,
    val rate: Double,
) {
    companion object {
        fun from(course: Course) = CourseSimpleResponse(
            courseId = course.id,
            title = course.title,
            description = course.description,
            category = course.category,
            imageUrl = course.imageUrl,
            rate = course.rate
        )
    }
}
