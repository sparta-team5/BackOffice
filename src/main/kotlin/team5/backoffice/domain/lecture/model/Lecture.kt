package team5.backoffice.domain.lecture.model

import jakarta.persistence.*
import team5.backoffice.domain.course.model.Course

@Entity
@Table(name = "lecture")
class Lecture(
    @Column(name = "title")
    var title: String,

    @Column(name = "video_url")
    var videoUrl: String,

    @ManyToOne
    @JoinColumn(name = "course_id")
    var course: Course,
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null
}