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
// TODO : 이 dto를 가지고 가공해서 그때그떄 맞는 response를 만들어 내야 함
// TODO : isBookmarked, isSubscribed, tutorFollowed 같은 정보를 studentId 를 받아 Boolean 형태로 반환해주는 로직도 필요할듯
// TODO : existsById(bookmarkId(courseId=CourseLowData.id, studentId)
// TODO : existsById(followId(tutorId=CourseLowData.tutor.id, studentId) 같은 형태