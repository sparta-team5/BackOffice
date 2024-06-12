package domain.course.model

import domain.course.dto.CourseListResponse
import domain.course.dto.CourseResponse
import domain.course.dto.CourseSimpleResponse
import jakarta.persistence.*
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

    @OneToMany(mappedBy = "course", orphanRemoval = true)
    val lectures: MutableList<Lecture> = mutableListOf(),

    @OneToMany(mappedBy = "course", orphanRemoval = true)
    val reviews: MutableList<Review> = mutableListOf(),

    @Column(name = "image_url", nullable = true)
    var imageUrl: String,

    @Column(name = "category", nullable = true)
    var category: String, // 카테고리가 중복 (Kotlin + Spring) 될 수 있을것 같은데 그부분은 그냥 넘겼습니다

    @Column(name = "view_count", nullable = false)
    var viewCount: Long,

    @Column(name = "rate")
    var rate: Double, // averageRate 가 되어야 맞는건지 생각이 듭니다
    // 백오피스 쪽 생각하면 rate 를 따로 테이블로 관리하고 해당 courseRate 의 average 만 표기하는게 맞지 않을까요?

    @Column(name = "created_at", nullable = false)
    val createdAt: LocalDateTime,

    ) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null
}

fun Course.toResponse(isBookMarked: Boolean, isSubscribed: Boolean): CourseResponse {
    return CourseResponse(
        title = title,
        tutor = tutor.name,
        category = category,
        description = description,
        isBookMarked = isBookMarked,
        isSubscribed = isSubscribed,
        lectures = lectures, //toResponse?
        reviews = reviews, //toResponse?
        viewCount = viewCount,
        rate = rate
    )
}

fun Course.toListResponse(isBookmarked: Boolean, isSubscribed: Boolean): CourseListResponse {
    return CourseListResponse(
        title = title,
        tutor = tutor.name,
        category = category,
        description = description,
        imageUrl = imageUrl,
        viewCount = viewCount,
        rate = rate
    )
}

fun Course.toSimpleResponse(): CourseSimpleResponse {
    return CourseSimpleResponse(
        title = title,
        description = description,
        category = category,
        imageUrl = imageUrl,
        rate = rate
    )
}
