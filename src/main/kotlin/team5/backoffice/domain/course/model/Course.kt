package team5.backoffice.domain.course.model

import jakarta.persistence.*
import team5.backoffice.domain.user.model.Tutor
import java.time.LocalDateTime

@Entity
@Table(name = "course")
class Course(

    @Column(name = "title", nullable = false)
    var title: String,

    @Column(name = "description", nullable = false)
    var description: String,

    @ManyToOne
    @JoinColumn(name = "tutor_id", nullable = false)
    val tutor: Tutor,

    @Column(name = "image_url", nullable = false)
    var imageUrl: String,

//    @ManyToOne
//    @JoinColumn(name = "category", nullable = false)
//    var category: Category,

    @Column(name = "view_count", nullable = false)
    var viewCount: Long,

    @Column(name = "created_at", nullable = false)
    val createdAt: LocalDateTime,

//    @Column(name = "rate", nullable = false)
//    val rate: Double,

    ) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null

    fun increaseViewCount() {
        viewCount += 1
    }
}
