package domain.lecture.dto

import domain.lecture.model.Lecture

data class LectureResponse(
    val id: Long,
    val courseId: Long,
    val title: String,
    val videoUrl: String,
){
    companion object {
        fun from(lecture: Lecture) = LectureResponse(
            id = lecture.id!!,
            courseId = lecture.courseId,
            title = lecture.title,
            videoUrl = lecture.videoUrl
        )
    }
}
