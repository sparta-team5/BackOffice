package team5.backoffice.domain.review.dto

import team5.backoffice.domain.review.model.Review

data class ReviewResponse(
    val id: Long,
    val studentId: Long,
    val courseId: Long,
    val body: String,
    val rate: Int
) {
    companion object {
        fun from(review: Review): ReviewResponse = ReviewResponse(
            id = review.id!!,
            studentId = review.student.id!!,
            courseId = review.course.id!!,
            body = review.body,
            rate = review.rate
        )
    }
}
