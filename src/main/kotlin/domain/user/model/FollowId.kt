package domain.user.model

import jakarta.persistence.Column
import jakarta.persistence.Embeddable
import java.io.Serializable

@Embeddable
data class FollowId( //for 복합키 사용
    @Column(name = "student_id")
    val studentId: Long,

    @Column(name = "tutor_id")
    val tutorId: Long

): Serializable
