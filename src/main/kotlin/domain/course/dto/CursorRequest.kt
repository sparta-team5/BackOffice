package domain.course.dto

import domain.course.model.SortType
import javax.swing.SortOrder

data class CursorRequest(
    val cursor: Int = 0,
    val page: Int = 10,
    val orderBy: SortType
)
