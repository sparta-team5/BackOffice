package domain.course.dto

import domain.course.model.Course
import domain.lecture.dto.LectureResponse
import domain.review.dto.ReviewResponse


data class CourseResponse(
    val id: Long,
    val title: String,
    val tutor: String,
    val category: String,
    val description: String,
    val lectures: List<LectureResponse>,
    val reviews: List<ReviewResponse>,
    val isBookMarked: Boolean = false,
    val isSubscribed: Boolean = false,
    val viewCount: Long,
    val rate: Double
) {
    companion object {
        fun from(course: Course, isBookMarked: Boolean, isSubscribed: Boolean) = CourseResponse(
            id = course.id!!,
            title = course.title,
            tutor = course.tutor.name,
            category = course.category,
            description = course.description,
            isBookMarked = isBookMarked,
            isSubscribed = isSubscribed,
            lectures = course.lectures.map { LectureResponse.from(it) },
            reviews = course.reviews.map { ReviewResponse.from(it) },
            viewCount = course.viewCount,
            rate = course.rate
        )
    }
}