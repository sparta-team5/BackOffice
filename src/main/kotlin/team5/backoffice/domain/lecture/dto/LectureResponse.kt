package team5.backoffice.domain.lecture.dto

import team5.backoffice.domain.lecture.model.Lecture

data class LectureResponse(
    val id: Long,
    val courseId: Long,
    val title: String,
    val videoUrl: String,
) {
    companion object {
        fun from(lecture: Lecture) = LectureResponse(
            id = lecture.id!!,
            courseId = lecture.course.id!!,
            title = lecture.title,
            videoUrl = lecture.videoUrl
        )
    }
}
