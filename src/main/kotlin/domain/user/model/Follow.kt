package domain.user.model

import jakarta.persistence.*

@Entity
@Table(name="follow")
class Follow(

    @EmbeddedId
    val id: FollowId,

    @ManyToOne
    @JoinColumn(name = "student_id", nullable = false)
    val student: Student,

    @ManyToOne
    @JoinColumn(name = "tutor_id", nullable = false)
    val tutor: Tutor

)