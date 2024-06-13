package team5.backoffice.domain.review.model

import team5.backoffice.domain.course.model.Course
import team5.backoffice.domain.user.model.Student
import jakarta.persistence.*


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
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null
}