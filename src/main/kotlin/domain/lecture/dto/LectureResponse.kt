package domain.lecture.dto

data class LectureResponse(
    val id: Long,
    val courseId: Long,
    val title: String,
    val videoUrl: String,
)