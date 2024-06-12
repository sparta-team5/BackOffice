package domain.review.model

import jakarta.persistence.*


@Entity
@Table(name = "review")
class Review(
    @Column(name = "student_id")
    val studentId: Long,

    @Column(name = "course_id")
    val courseId: Long,

    @Column(name = "body")
    var body: String,

    @Column(name = "rate")
    var rate: Int,
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null
}