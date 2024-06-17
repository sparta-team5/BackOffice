package team5.backoffice.domain.course.dto

import team5.backoffice.domain.course.model.OrderType
import java.time.LocalDateTime

data class CursorRequest(
    val cursorTime : LocalDateTime?,
    val cursorView : Long?,
    val cursorOrderType : OrderType,
)
