package domain.review.dto

import domain.review.model.Review

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
            studentId = review.studentId,
            courseId = review.courseId,
            body = review.body,
            rate = review.rate
        )
    }
}
