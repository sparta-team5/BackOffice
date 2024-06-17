package team5.backoffice.domain.course.dto

data class TutorLowData(
    val amountView : Long,
    val amountCourse : Long,
    val amountSubscription: Long,
    val amountBookmark: Long,
    val amountFollow: Long,
    val averageRate: Double,
)
