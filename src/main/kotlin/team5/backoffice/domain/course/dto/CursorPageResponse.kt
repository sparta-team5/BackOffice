package team5.backoffice.domain.course.dto

import team5.backoffice.domain.course.dto.CourseListResponse

data class CursorPageResponse(
    val page: List<CourseListResponse>,
    val nextCursor: Int?
)
