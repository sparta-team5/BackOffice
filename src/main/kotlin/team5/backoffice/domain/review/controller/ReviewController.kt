package team5.backoffice.domain.review.controller

import team5.backoffice.domain.review.dto.ReviewRequest
import team5.backoffice.domain.review.dto.ReviewResponse
import team5.backoffice.domain.review.service.ReviewService
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
        @PathVariable("courseId") courseId: Long
    ): ResponseEntity<List<ReviewResponse>> {
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(reviewService.getAllReviewsByCourse(courseId))
    }

    @PostMapping
    fun addReview(
        @PathVariable("courseId") courseId: Long,
        @RequestBody request: ReviewRequest
    ): ResponseEntity<ReviewResponse> {
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(reviewService.addReview(courseId, request))
    }

    @PutMapping("/{reviewId}")
    fun updateReview(
        @PathVariable("courseId") courseId: Long,
        @PathVariable("reviewId") reviewId: Long,
        @RequestBody request: ReviewRequest
    ): ResponseEntity<ReviewResponse> {
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(reviewService.updateReview(courseId, reviewId, request))
    }

    @DeleteMapping("/{reviewId}")
    fun deleteReview(
        @PathVariable("courseId") courseId: Long,
        @PathVariable("reviewId") reviewId: Long,
    ): ResponseEntity<Unit> {
        reviewService.deleteReview(courseId, reviewId)
        return ResponseEntity
            .status(HttpStatus.NO_CONTENT)
            .build()
    }
}