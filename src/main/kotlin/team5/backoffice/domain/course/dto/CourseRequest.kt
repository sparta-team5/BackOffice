package team5.backoffice.domain.course.dto

import team5.backoffice.domain.course.model.Category

data class CourseRequest(
    val tutorId: Long,
    val title: String,
    val description: String,
    val imageUrl: String,
    val category: Category,
)
