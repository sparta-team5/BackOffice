package domain.lecture.model

import jakarta.persistence.*

@Entity
@Table(name = "lecture")
class Lecture(
    @Column(name = "title")
    var title: String,

    @Column(name = "video_url")
    var videoUrl: String,

    @Column(name = "course_id")
    var courseId: Long,
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null
}