package domain.course.dto

data class CursorRequest(
    val cursor: Int = 0,
    val page: Int = 10,
    val orderBy: String = "createdAt", // createdAt, viewCount, rate + desc
)
