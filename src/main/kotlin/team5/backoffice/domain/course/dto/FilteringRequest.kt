package team5.backoffice.domain.course.dto

import team5.backoffice.domain.course.model.OrderType

data class FilteringRequest(
    val word: String?,
    val category: String?,
    val rate: Double?,
    val viewCount: Long?,
    val orderType: OrderType,
    val bookmarkCount: Long?,
    val subscriptionCount: Long?,
)
