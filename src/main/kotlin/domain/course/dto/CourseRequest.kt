package domain.course.dto

data class CourseRequest(
    val title: String,
    val description: String,
    val imageUrl: String,
    val category: String,
)
