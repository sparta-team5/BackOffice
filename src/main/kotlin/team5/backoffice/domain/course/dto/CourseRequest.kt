package team5.backoffice.domain.course.dto

data class CourseRequest(
    val tutorId: Long,
    val title: String,
    val description: String,
    val imageUrl: String,
//    val category: String,
)
