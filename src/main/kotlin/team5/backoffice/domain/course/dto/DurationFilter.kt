package team5.backoffice.domain.course.dto

import java.time.Duration
import java.time.LocalDateTime

data class DurationFilter(
    val viewDate: LocalDateTime?,
    val bookmarkDate: LocalDateTime?,
    val subscriptionDate: LocalDateTime?,
    val followUpDate: LocalDateTime?,
    val courseDate: LocalDateTime?,
)
