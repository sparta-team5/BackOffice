package domain.review.controller

import domain.review.dto.AddReviewRequest
import domain.review.dto.ReviewResponse
import domain.review.dto.UpdateReviewRequest
import domain.review.service.ReviewService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/courses/{courseId}/reviews")
class ReviewController(
    private val reviewService: ReviewService
) {
    @GetMapping
    fun getReviewsByCourse(
        @PathVariable("courseId") courseId: String
    ): ResponseEntity<List<ReviewResponse>> {
        return ResponseEntity.ok(reviewService.getAllReviewsByCourse())
    }

    @PostMapping
    fun addReview(
        @PathVariable("courseId") courseId: Long,
        @RequestBody addReviewRequest: AddReviewRequest
    ): ResponseEntity<ReviewResponse> {
        return ResponseEntity.status(
            HttpStatus.CREATED
        ).body(
            reviewService.addReview(courseId, addReviewRequest)
        )
    }

    @PutMapping("/{reviewId}")
    fun updateReview(
        @PathVariable("courseId") courseId: Long,
        @PathVariable("reviewId") reviewId: Long,
        @RequestBody updateReviewRequest: UpdateReviewRequest
    ): ResponseEntity<ReviewResponse> {
        return ResponseEntity.ok(reviewService.updateReview(courseId, reviewId, updateReviewRequest))
    }

    @DeleteMapping("/{reviewId}")
    fun deleteReview(
        @PathVariable("courseId") courseId: Long,
        @PathVariable("reviewId") reviewId: Long,
    ): ResponseEntity<Unit> {
        reviewService.deleteReview(courseId, reviewId)
        return ResponseEntity.noContent().build()
    }
}