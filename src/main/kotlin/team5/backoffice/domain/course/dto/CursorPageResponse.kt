package team5.backoffice.domain.course.dto

data class CursorPageResponse(
    val page: List<CourseListResponse>,
    val nextCursor: Int?
)
