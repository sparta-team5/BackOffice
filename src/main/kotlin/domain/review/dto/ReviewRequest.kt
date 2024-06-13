package domain.review.dto

data class ReviewRequest(
    val body: String,
    val rate: Int,
)
