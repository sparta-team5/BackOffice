package domain.user.controller

import domain.review.service.ReviewService
import domain.user.dto.TutorResponseDto
import domain.user.dto.UpdateTutorRequestDto
import domain.user.service.UserService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/tutors")
class TutorController(
    private val userService : UserService,
    private val reviewService : ReviewService,
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
    fun getTutorDetail(@PathVariable tutorId :Long):ResponseEntity<TutorResponseDto>{
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(userService.getTutorById(tutorId))
    }

    @PutMapping("/{tutorId}")
    fun updateTutor(
        @PathVariable tutorId :Long,
    @RequestBody updateTutorRequest : UpdateTutorRequestDto
    ):ResponseEntity<TutorResponseDto>{

        return try {
            ResponseEntity
                .status(HttpStatus.OK)
                .body(userService.updateTutorById(tutorId, updateTutorRequest))
        } catch (e: IllegalAccessException) {
            ResponseEntity.status(403).build()
        }
    }

    @DeleteMapping("/{tutorId}")
    fun deleteTutor(@PathVariable tutorId: Long): ResponseEntity<Unit> {

        return try {
            ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .body(userService.deleteTutorById(tutorId))
        } catch (e: IllegalAccessException) {
            ResponseEntity
                .status(403).build()
        }
    }

    @GetMapping("/{tutorId}/reviews")
    fun getAllReviewsByTutor(@PathVariable tutorId: Long):ResponseEntity<List<>> {

        return ResponseEntity
            .status(HttpStatus.OK)
            .body(reviewService.getAllReviewsByTutor(tutorId))
    }//todo : 리뷰 response dto 이름 물어보기

    //todo
//    @PostMapping("/{tutorId}/follow")
//    fun followTutor(
//        @PathVariable tutorId :Long,
//    ): ResponseEntity<TutorResponseDto>{
//        return try {
//            ResponseEntity
//                .status(HttpStatus.OK)
//                .body(userService.followStudentAndUser(tutorId))
//        } catch (e: IllegalAccessException) {
//            ResponseEntity
//                .status(403).build()
//        }
//    }
//
//    @DeleteMapping("/{tutorId}/follow")
//    fun unfollowTutor(
//        @PathVariable tutorId :Long,
//    ):ResponseEntity<TutorResponseDto>{
//        return try {
//            ResponseEntity
//                .status(HttpStatus.OK)
//                .body(userService.unfollowStudentAndUser(tutorId))
//        } catch (e: IllegalAccessException) {
//            ResponseEntity
//                .status(403).build()
//        }
//    }











}