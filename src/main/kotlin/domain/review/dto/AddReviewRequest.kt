package domain.review.dto

data class AddReviewRequest(
    val courseId: Long,
    val body: String,
    val rate: Int,
)
