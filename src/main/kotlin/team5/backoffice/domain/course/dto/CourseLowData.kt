package team5.backoffice.domain.course.dto

import java.time.LocalDateTime

data class CourseLowData (
    val id : Long,
    val title : String,
    val description : String,
    val tutorName : String,
    val categoryName : String,
    val imageUrl : String,
    val createdAt : LocalDateTime,
    val rate : Double,
    val viewCount : Long,
    val bookmarkCount : Long,
    val subscribeCount : Long,
)