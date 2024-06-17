package team5.backoffice.domain.course.dto

import team5.backoffice.domain.user.model.Tutor
import java.time.LocalDateTime

data class CourseLowData (
    val id : Long,
    val title : String,
    val description : String,
    val tutor : Tutor,
    val categoryName : String,
    val imageUrl : String,
    val createdAt : LocalDateTime,
    val rate : Double,
    val viewCount : Long,
    val bookmarkCount : Long,
    val subscribeCount : Long,
)