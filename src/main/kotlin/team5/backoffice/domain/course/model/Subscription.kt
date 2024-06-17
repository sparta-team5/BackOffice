package team5.backoffice.domain.course.model

import jakarta.persistence.*
import team5.backoffice.domain.user.model.Student
import java.time.LocalDateTime


@Entity
@Table(name = "subscription")
class Subscription(
    @EmbeddedId
    val id: SubscriptionId,

    @Column(name = "created_at")
    val createdAt: LocalDateTime,

    @ManyToOne
    @JoinColumn(name = "course_id", insertable = false, updatable = false)
    val course: Course,

    @ManyToOne
    @JoinColumn(name = "student_id", insertable = false, updatable = false)
    val student: Student,
)