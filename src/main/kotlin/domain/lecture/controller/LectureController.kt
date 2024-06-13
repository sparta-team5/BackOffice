package domain.lecture.controller

import domain.lecture.dto.CreateLectureRequest
import domain.lecture.dto.LectureResponse
import domain.lecture.dto.UpdateLectureRequest
import domain.lecture.service.LectureService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/courses/{courseId}/lectures")
class LectureController(
    private val lectureService: LectureService
) {

    @GetMapping("/{lectureId}")
    fun getLecture(
        @PathVariable courseId: Long,
        @PathVariable lectureId: Long,
    ): ResponseEntity<LectureResponse> {
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(lectureService.getLecture(courseId, lectureId))
    }

    @PostMapping
    fun createLecture(
        @PathVariable courseId: Long,
        @RequestBody createLectureRequest: CreateLectureRequest,
    ): ResponseEntity<LectureResponse> {
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(lectureService.addLecture(courseId, createLectureRequest))
    }

    @PutMapping("/{lectureId}")
    fun updateLecture(
        @PathVariable courseId: Long,
        @PathVariable lectureId: Long,
        @RequestBody updateLectureRequest: UpdateLectureRequest
    ): ResponseEntity<LectureResponse> {
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(lectureService.updateLecture(courseId, lectureId, updateLectureRequest))
    }

    @DeleteMapping("/{lectureId}")
    fun deleteLecture(
        @PathVariable courseId: Long,
        @PathVariable lectureId: Long,
    ): ResponseEntity<Unit> {
        lectureService.deleteLecture(courseId, lectureId)
        return ResponseEntity
            .status(HttpStatus.NO_CONTENT)
            .build()
    }
}