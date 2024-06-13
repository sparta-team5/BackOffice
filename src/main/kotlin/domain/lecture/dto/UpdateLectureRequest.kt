package domain.lecture.dto

data class UpdateLectureRequest(
    val courseId: Long,
    val title: String,
    val videoUrl: String,
)
