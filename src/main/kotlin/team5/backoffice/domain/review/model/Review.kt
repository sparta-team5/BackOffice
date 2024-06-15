package team5.backoffice.domain.review.model

import team5.backoffice.domain.course.model.Course
import team5.backoffice.domain.user.model.Student
import jakarta.persistence.*
import java.time.LocalDateTime


@Entity
@Table(name = "review")
class Review(

    @ManyToOne
    @JoinColumn(name = "student_id")
    val student: Student,

    @ManyToOne
    @JoinColumn(name = "course_id")
    val course: Course,

    @Column(name = "body")
    var body: String,

    @Column(name = "rate")
    var rate: Int,

    @Column(name = "created_at")
    val createdAt: LocalDateTime,
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null
}