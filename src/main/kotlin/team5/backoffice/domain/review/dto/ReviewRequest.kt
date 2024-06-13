package team5.backoffice.domain.review.dto

data class ReviewRequest(
    val studentId: Long,
    val body: String,
    val rate: Int,
)
