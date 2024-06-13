package domain.review.service

import domain.course.repository.CourseRepository
import domain.review.dto.ReviewRequest
import domain.review.dto.ReviewResponse
import domain.review.model.Review
import domain.review.repository.ReviewRepository
import domain.user.model.Student
import domain.user.repository.StudentRepository
import domain.user.repository.TutorRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class ReviewService(
    private val reviewRepository: ReviewRepository,
    private val courseRepository: CourseRepository,
    private val tutorRepository: TutorRepository,
    private val studentRepository: StudentRepository,
) {

    fun addReview(courseId: Long, request: ReviewRequest): ReviewResponse {
        val course = courseRepository.findByIdOrNull(courseId) ?: throw RuntimeException("course not found")
        return Review(
            course = course,
            student = Student("test", "test@gmail.com"), // TODO student id 가져오기
            body = request.body,
            rate = request.rate,

            ).let { ReviewResponse.from(it) }
    }

    fun updateReview(courseId: Long, reviewId: Long, request: ReviewRequest): ReviewResponse {
        val review =
            reviewRepository.findByIdAndCourseId(courseId, reviewId) ?: throw RuntimeException("review not found")
        // TODO review 작성자가 요청을 보낸 사람과 같은지 확인
        review.apply {
            this.body = request.body
            this.rate = request.rate
        }
        return reviewRepository.save(review).let { ReviewResponse.from(it) }
    }

    fun deleteReview(courseId: Long, reviewId: Long): Unit {
        val review =
            reviewRepository.findByIdAndCourseId(courseId, reviewId) ?: throw RuntimeException("review not found")
        // TODO review 작성자가 요청을 보낸 사람과 같은지 확인
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