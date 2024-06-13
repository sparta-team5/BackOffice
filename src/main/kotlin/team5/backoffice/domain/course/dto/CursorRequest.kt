package team5.backoffice.domain.course.dto

import team5.backoffice.domain.course.model.SortType

data class CursorRequest(
    val cursor: Int,
    val page: Int,
    val orderBy: SortType
)
