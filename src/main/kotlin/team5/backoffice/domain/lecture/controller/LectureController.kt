package team5.backoffice.domain.lecture.controller

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.*
import team5.backoffice.domain.lecture.dto.CreateLectureRequest
import team5.backoffice.domain.lecture.dto.LectureResponse
import team5.backoffice.domain.lecture.dto.UpdateLectureRequest
import team5.backoffice.domain.lecture.service.LectureService
import team5.backoffice.infra.security.UserPrincipal

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
    @PreAuthorize("hasRole('ROLE_TUTOR')")
    fun createLecture(
        @PathVariable courseId: Long,
        @RequestBody createLectureRequest: CreateLectureRequest,
        authentication: Authentication
    ): ResponseEntity<LectureResponse> {
        val tutor = authentication.principal as UserPrincipal
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(lectureService.addLecture(courseId, createLectureRequest, tutor.id))
    }

    @PutMapping("/{lectureId}")
    @PreAuthorize("hasRole('ROLE_TUTOR')")
    fun updateLecture(
        @PathVariable courseId: Long,
        @PathVariable lectureId: Long,
        @RequestBody updateLectureRequest: UpdateLectureRequest,
        authentication: Authentication
    ): ResponseEntity<LectureResponse> {
        val tutor = authentication.principal as UserPrincipal
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(lectureService.updateLecture(courseId, lectureId, updateLectureRequest, tutor.id))
    }

    @DeleteMapping("/{lectureId}")
    @PreAuthorize("hasRole('ROLE_TUTOR')")
    fun deleteLecture(
        @PathVariable courseId: Long,
        @PathVariable lectureId: Long,
        authentication: Authentication
    ): ResponseEntity<Unit> {
        val tutor = authentication.principal as UserPrincipal
        lectureService.deleteLecture(courseId, lectureId, tutor.id)
        return ResponseEntity
            .status(HttpStatus.NO_CONTENT)
            .build()
    }
}