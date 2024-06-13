package domain.user.controller

import domain.review.service.ReviewService
import domain.user.dto.TutorResponse
import domain.user.dto.UpdateTutorRequest
import domain.user.service.UserService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

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
    fun getTutorDetail(@PathVariable tutorId: Long): ResponseEntity<TutorResponse> {
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(userService.getTutorById(tutorId))
    }

    @PutMapping("/{tutorId}")
    fun updateTutor(
        @PathVariable tutorId: Long,
        @RequestBody updateTutorRequest: UpdateTutorRequest
    ): ResponseEntity<TutorResponse> {

        return ResponseEntity
            .status(HttpStatus.OK)
            .body(userService.updateTutorById(tutorId, updateTutorRequest))
    }

    @DeleteMapping("/{tutorId}")
    fun deleteTutor(@PathVariable tutorId: Long): ResponseEntity<Unit> {

        return ResponseEntity
            .status(HttpStatus.NO_CONTENT)
            .body(userService.deleteTutorById(tutorId))

    }

    @GetMapping("/{tutorId}/reviews")
    fun getAllReviewsByTutor(@PathVariable tutorId: Long): ResponseEntity<List<ReviewResponse>> {

        return ResponseEntity
            .status(HttpStatus.OK)
            .body(reviewService.getAllReviewsByTutor(tutorId))
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