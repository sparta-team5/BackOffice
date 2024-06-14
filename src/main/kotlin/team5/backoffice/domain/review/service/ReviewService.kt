package team5.backoffice.domain.review.service

import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import team5.backoffice.domain.course.repository.CourseRepository.CourseRepository
import team5.backoffice.domain.review.dto.ReviewRequest
import team5.backoffice.domain.review.dto.ReviewResponse
import team5.backoffice.domain.review.model.Review
import team5.backoffice.domain.review.repository.ReviewRepository
import team5.backoffice.domain.user.repository.StudentRepository
import team5.backoffice.domain.user.repository.TutorRepository

@Service
class ReviewService(
    private val reviewRepository: ReviewRepository,
    private val courseRepository: CourseRepository,
    private val tutorRepository: TutorRepository,
    private val studentRepository: StudentRepository,
) {

    fun addReview(courseId: Long, request: ReviewRequest, studentId: Long): ReviewResponse {
        val course = courseRepository.findByIdOrNull(courseId) ?: throw RuntimeException("course not found")
        val student = studentRepository.findByIdOrNull(studentId) ?: throw RuntimeException("student not found")
        return Review(
            course = course,
            student = student,
            body = request.body,
            rate = request.rate,
        ).let { reviewRepository.save(it) }
            .let { ReviewResponse.from(it) }
    }

    fun updateReview(courseId: Long, reviewId: Long, request: ReviewRequest, studentId: Long): ReviewResponse {
        val review =
            reviewRepository.findByIdAndCourseId(reviewId, courseId) ?: throw RuntimeException("review not found")
        if (review.student.id != studentId) throw RuntimeException()

        review.apply {
            this.body = request.body
            this.rate = request.rate
        }
        return ReviewResponse.from(review)
    }

    fun deleteReview(courseId: Long, reviewId: Long, studentId: Long) {
        val review =
            reviewRepository.findByIdAndCourseId(reviewId, courseId) ?: throw RuntimeException("review not found")
        if (review.student.id != studentId) throw RuntimeException()
        reviewRepository.delete(review)
    }

    fun getAllReviewsByCourse(courseId: Long): List<ReviewResponse> {
        courseRepository.findByIdOrNull(courseId) ?: throw RuntimeException("course not found")
        return reviewRepository.findAllByCourseId(courseId).map { ReviewResponse.from(it) }
    }

    fun getAllReviewsByTutor(tutorId: Long): List<ReviewResponse> {
        tutorRepository.findByIdOrNull(tutorId) ?: throw RuntimeException("tutor not found")
        // TODO
        return listOf()
    }


}