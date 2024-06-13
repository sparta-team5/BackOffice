package team5.backoffice.domain.lecture.controller

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import team5.backoffice.domain.lecture.dto.CreateLectureRequest
import team5.backoffice.domain.lecture.dto.LectureResponse
import team5.backoffice.domain.lecture.dto.UpdateLectureRequest
import team5.backoffice.domain.lecture.service.LectureService

@RestController
@RequestMapping("/courses/{courseId}/lectures")
class LectureController(
    private val lectureService: LectureService
) {
    @GetMapping
    fun getAllLecture(
        @PathVariable("courseId") courseId: Long,
    ): ResponseEntity<List<LectureResponse>> {
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(lectureService.getAllLecture(courseId))
    }

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