package team5.backoffice.domain.course.model

import jakarta.persistence.Column
import jakarta.persistence.Embeddable
import java.io.Serializable

@Embeddable
data class SubscriptionId( //for 복합키 사용
    @Column(name = "student_id")
    val studentId: Long,

    @Column(name = "course_id")
    val courseId: Long

) : Serializable
