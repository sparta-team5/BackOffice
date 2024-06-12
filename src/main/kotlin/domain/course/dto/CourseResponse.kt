package domain.course.dto

import domain.course.model.Course


data class CourseResponse(
    val courseId: Long?,
    val title: String,
    val tutor: String,
    val category: String,
    val description: String,
    val lectures: List<Lecture>,
    val reviews: List<Leview>,
    val isBookMarked: Boolean = false,
    val isSubscribed: Boolean = false,
    val viewCount: Long,
    val rate: Double
) {
    companion object {
        fun from(course: Course, isBookMarked: Boolean, isSubscribed: Boolean) = CourseResponse(
            courseId = course.id,
            title = course.title,
            tutor = course.tutor.name,
            category = course.category,
            description = course.description,
            isBookMarked = isBookMarked,
            isSubscribed = isSubscribed,
            lectures = course.lectures, //toResponse?
            reviews = course.reviews, //toResponse?
            viewCount = course.viewCount,
            rate = course.rate
        )
    }
}