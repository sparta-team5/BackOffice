package domain.review.repository

interface ReviewRepository {
    fun findByCourseIdAndId(courseId: Long, id: Long) : Unit
}