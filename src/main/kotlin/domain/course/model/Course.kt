package domain.course.model

import domain.course.dto.CourseListResponse
import domain.course.dto.CourseRequest
import domain.course.dto.CourseResponse
import domain.course.dto.CourseSimpleResponse
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "course")
class Course(

    @Column(name = "title")
    var title: String,

    @Column(name = "description")
    var description: String,

    @ManyToOne
    @JoinColumn(name = "tutor_id")
    val tutor: Tutor,

    @OneToMany(mappedBy = "course")
    val lectures: MutableList<Lecture> = mutableListOf(),

    @OneToMany(mappedBy = "course")
    val reviews: MutableList<Review> = mutableListOf(),

    @Column(name = "image_url")
    var imageUrl: String,

    @Column(name = "category")
    var category: Category,

    @Column(name = "view_count")
    var viewCount: Long,

    @Column(name = "rate")
    var rate: Int?,

    @Column(name = "created_at")
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

fun Course.toSimpleResponse() : CourseSimpleResponse {
    return CourseSimpleResponse(
        title = title,
        description = description,
        category = category,
        imageUrl = imageUrl,
    )
}
