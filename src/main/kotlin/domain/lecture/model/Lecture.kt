package domain.lecture.model

import domain.course.model.Course
import jakarta.persistence.*

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