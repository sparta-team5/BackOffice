package team5.backoffice.domain.review.controller

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.*
import team5.backoffice.domain.review.dto.ReviewRequest
import team5.backoffice.domain.review.dto.ReviewResponse
import team5.backoffice.domain.review.service.ReviewService
import team5.backoffice.infra.security.UserPrincipal

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
    @PreAuthorize("hasRole('ROLE_STUDENT')")
    fun addReview(
        @PathVariable("courseId") courseId: Long,
        @RequestBody request: ReviewRequest,
        authentication: Authentication
    ): ResponseEntity<ReviewResponse> {
        val student = authentication.principal as UserPrincipal
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(reviewService.addReview(courseId, request, student.id))
    }

    @PutMapping("/{reviewId}")
    @PreAuthorize("hasRole('ROLE_STUDENT')")
    fun updateReview(
        @PathVariable("courseId") courseId: Long,
        @PathVariable("reviewId") reviewId: Long,
        @RequestBody request: ReviewRequest,
        authentication: Authentication
    ): ResponseEntity<ReviewResponse> {
        val student = authentication.principal as UserPrincipal
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(reviewService.updateReview(courseId, reviewId, request, student.id))
    }

    @DeleteMapping("/{reviewId}")
    @PreAuthorize("hasRole('ROLE_STUDENT')")
    fun deleteReview(
        @PathVariable("courseId") courseId: Long,
        @PathVariable("reviewId") reviewId: Long,
        authentication: Authentication
    ): ResponseEntity<Unit> {
        val student = authentication.principal as UserPrincipal
        reviewService.deleteReview(courseId, reviewId, student.id)
        return ResponseEntity
            .status(HttpStatus.NO_CONTENT)
            .build()
    }
}