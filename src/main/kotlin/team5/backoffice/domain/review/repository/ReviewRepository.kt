package team5.backoffice.domain.review.repository

import org.springframework.data.jpa.repository.JpaRepository
import team5.backoffice.domain.review.model.Review

interface ReviewRepository : JpaRepository<Review, Long> {
    fun findByIdAndCourseId(reviewId: Long, courseId: Long): Review?
    fun findAllByCourseId(courseId: Long): List<Review>
}