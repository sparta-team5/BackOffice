package domain.course.dto

import domain.course.model.Course

data class CourseSimpleResponse(
    val id: Long,
    val title: String,
    val description: String,
    val imageUrl: String,
    val category: String,
) {
    companion object {
        fun from(course: Course) = CourseSimpleResponse(
            id = course.id!!,
            title = course.title,
            description = course.description,
            category = course.category,
            imageUrl = course.imageUrl,
        )
    }
}
