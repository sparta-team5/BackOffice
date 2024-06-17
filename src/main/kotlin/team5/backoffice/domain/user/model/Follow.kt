package team5.backoffice.domain.user.model

import jakarta.persistence.*

@Entity
@Table(name = "follow")
class Follow(

    @EmbeddedId
    val id: FollowId,

    @ManyToOne
    @JoinColumn(name = "tutor_id", insertable = false, updatable = false)
    val tutor: Tutor,

    @ManyToOne
    @JoinColumn(name = "student_id", insertable = false, updatable = false)
    val student: Student,
    )