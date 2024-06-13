package team5.backoffice.domain.review.repository

import team5.backoffice.domain.review.model.Review
import org.springframework.data.jpa.repository.JpaRepository

interface ReviewRepository : JpaRepository<Review, Long> {
    fun findByIdAndCourseId(reviewId: Long, courseId: Long): Review?
    fun findAllByCourseId(courseId: Long): List<Review>
}