package team5.backoffice.domain.course.dto

import team5.backoffice.domain.course.model.Course
import java.time.LocalDateTime


data class CourseResponse(
    val id: Long,
    val title: String,
    val tutor: String,
    val category: String,
    val description: String,
    val isBookMarked: Boolean = false,
    val isSubscribed: Boolean = false,
    val viewCount: Long,
    val createdAt: LocalDateTime,
    val rate: Double,
//    val lectures : List<LectureResponse>,
//    val reviews : List<ReviewResponse>
) {
    companion object {
        fun from(course: Course, isBookMarked: Boolean, isSubscribed: Boolean, rate: Double, viewCount: Long) = CourseResponse(
            id = course.id!!,
            title = course.title,
            tutor = course.tutor.nickname,
            category = course.category.name,
            description = course.description,
            isBookMarked = isBookMarked,
            isSubscribed = isSubscribed,
            viewCount = viewCount,
            createdAt = course.createdAt,
            rate = rate,
        )
    }
}