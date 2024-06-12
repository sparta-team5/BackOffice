package domain.review.dto

data class UpdateReviewRequest(
    val body: String,
    val rate: Int,
)
