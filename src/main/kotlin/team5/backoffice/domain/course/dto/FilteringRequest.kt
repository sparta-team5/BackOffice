package team5.backoffice.domain.course.dto

import team5.backoffice.domain.course.model.Category
import team5.backoffice.domain.course.model.OrderType

data class FilteringRequest(
    val title: String,
    val description: String,
    val tutorNickName: String,
    val category: Category,
    val rate: Double,
    val viewCount: Long,
    val orderType: OrderType,
    val bookmarkCount: Long,
    val subscriptionCount: Long,
)
