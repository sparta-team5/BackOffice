package team5.backoffice.domain.user.controller

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.*
import team5.backoffice.domain.review.dto.ReviewResponse
import team5.backoffice.domain.review.service.ReviewService
import team5.backoffice.domain.user.dto.TutorResponse
import team5.backoffice.domain.user.dto.UpdateTutorRequest
import team5.backoffice.domain.user.service.UserService
import team5.backoffice.infra.security.UserPrincipal

@RestController
@RequestMapping("/tutors")
class TutorController(
    private val userService: UserService,
    private val reviewService: ReviewService,
) {

//    @PostMapping()
//    fun createTutor( //todo: 가입요청 ? admin 승인 ?
//        @RequestBody createTutorRequest: CreateTutorRequestDto
//    ):ResponseEntity<TutorResponseDto> {
//
//        return ResponseEntity
//            .status(HttpStatus.CREATED)
//            .body(userService.createTutor(createTutorRequest))
//    }

    @GetMapping("/{tutorId}")
    @PreAuthorize("hasRole('ROLE_TUTOR')")
    fun getTutorDetail(@PathVariable tutorId: Long): ResponseEntity<TutorResponse> {
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(userService.getTutorById(tutorId))
    }

    @PutMapping("/{tutorId}")
    @PreAuthorize("hasRole('ROLE_TUTOR')")
    fun updateTutor(
        @PathVariable tutorId: Long,
        @RequestBody updateTutorRequest: UpdateTutorRequest,
        authentication: Authentication
    ): ResponseEntity<TutorResponse> {
        val tutor = authentication.principal as UserPrincipal
        if (tutorId == tutor.id) {
            return ResponseEntity
                .status(HttpStatus.OK)
                .body(userService.updateTutorById(tutor.id, updateTutorRequest))
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build()
    }

    @DeleteMapping("/{tutorId}")
    @PreAuthorize("hasRole('ROLE_TUTOR')")
    fun deleteTutor(@PathVariable tutorId: Long, authentication: Authentication): ResponseEntity<Unit> {
        val tutor = authentication.principal as UserPrincipal
        if (tutorId == tutor.id) {
            return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .body(userService.deleteTutorById(tutor.id))
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build()
    }

    @GetMapping("/{tutorId}/reviews")
    fun getAllReviewsByTutor(@PathVariable tutorId: Long): ResponseEntity<List<ReviewResponse>> {
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(reviewService.getAllReviewsByTutor(tutorId))
    }
}

//todo
//    @PostMapping("/{tutorId}/follow")
//    fun followTutor(
//        @PathVariable tutorId :Long,
//    ): ResponseEntity<TutorResponseDto>{
//        return ResponseEntity
//                .status(HttpStatus.OK)
//                .body(userService.followStudentAndUser(tutorId))
//    }
//
//    @DeleteMapping("/{tutorId}/follow")
//    fun unfollowTutor(
//        @PathVariable tutorId :Long,
//    ):ResponseEntity<TutorResponseDto>{
//        return  ResponseEntity
//                .status(HttpStatus.OK)
//                .body(userService.unfollowStudentAndUser(tutorId))
//
//
//}