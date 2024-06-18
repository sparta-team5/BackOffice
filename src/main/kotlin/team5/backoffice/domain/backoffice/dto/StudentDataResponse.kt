package team5.backoffice.domain.backoffice.dto

data class StudentDataResponse(
    val studentId: Long,
    val studentName: String,
    val viewCount: Long,
    val bookingCount: Long,
    val subscriptionCount: Long,
    val isMyFollower: Boolean
) {
    companion object {
        fun from(student: StudentData, isMyFollower: Boolean) = StudentDataResponse(
            student.studentId,
            student.studentName,
            student.viewCount,
            bookingCount = student.bookingCount,
            subscriptionCount = student.subscriptionCount,
            isMyFollower = isMyFollower
        )
    }
}
