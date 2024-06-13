package domain.review.dto

data class AddReviewRequest(
    val body: String,
    val rate: Int,
)
