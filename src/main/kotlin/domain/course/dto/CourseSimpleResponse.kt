package domain.course.dto

data class CourseSimpleResponse(
    val title: String,
    val description: String,
    val imageUrl: String,
    val category: Category,
    val rate: Int?,
)
