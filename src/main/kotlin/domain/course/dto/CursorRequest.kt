package domain.course.dto

import domain.course.model.SortType
import javax.swing.SortOrder

data class CursorRequest(
    val cursor: Int,
    val page: Int,
    val orderBy: SortType
)
