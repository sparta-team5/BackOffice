package team5.backoffice.domain.backoffice.dto

import team5.backoffice.domain.course.model.OrderType
import java.time.LocalDateTime

data class StudentBackOfficeFilters(
    val duration : LocalDateTime?,
    val orderType: OrderType,
    val viewCount: Long?,
    val bookingCount: Long?,
    val subscriptionCount: Long?,
    val rateLimit: Double?
)
