package domain.review.dto

data class ReviewResponse(
    val id: Long,
    val courseId: Long,
    val body: String,
    val rate: Int
)
