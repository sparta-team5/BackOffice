package team5.backoffice.domain.course.model

import jakarta.persistence.*
import team5.backoffice.domain.user.model.Student
import java.time.LocalDateTime

@Entity
@Table(name = "view")
class View(

    @Column(name = "created_at")
    val createdAt : LocalDateTime,

    @ManyToOne
    @JoinColumn(name = "course_id")
    val course : Course,

    @ManyToOne
    @JoinColumn(name = "student_id", insertable = true, updatable = false)
    val student : Student
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id:Long? = null
}
