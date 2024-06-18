package team5.backoffice.domain.backoffice.dto

data class StudentData(
    val studentId: Long,
    val studentName: String,
    val viewCount: Long,
    val bookingCount: Long,
    val subscriptionCount: Long,
    val avgRate: Double,
)
