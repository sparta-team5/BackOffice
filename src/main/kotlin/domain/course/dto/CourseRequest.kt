package domain.course.dto

import domain.course.model.Category

data class CourseRequest(
    val title: String,
    val description: String,
    val imageUrl: String,
    val category: Category,
)
