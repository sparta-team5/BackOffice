package domain.course.dto

data class CursorPageResponse(
    val page: List<CourseListResponse>,
    val nextCursor: Int?
)
