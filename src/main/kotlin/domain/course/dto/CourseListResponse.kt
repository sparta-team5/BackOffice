package domain.course.dto

data class CourseListResponse(
    val title: String,
    val tutor: String,
    val description: String,
    val imageUrl: String,
    val category: Category,
)
