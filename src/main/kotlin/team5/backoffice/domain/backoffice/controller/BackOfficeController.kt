package team5.backoffice.domain.backoffice.controller

import org.springframework.data.domain.Pageable
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import team5.backoffice.domain.backoffice.dto.*
import team5.backoffice.domain.backoffice.service.BackOfficeService
import team5.backoffice.domain.course.dto.CourseLowData
import team5.backoffice.domain.course.dto.DurationFilter
import team5.backoffice.infra.security.UserPrincipal

@RestController
@RequestMapping(("/back-office"))
class BackOfficeController(
    private val backOfficeService: BackOfficeService
) {

    @GetMapping("/courses")
    @PreAuthorize("hasRole('TUTOR')")
    fun getMyCoursesData(
        authentication: Authentication,
        pageable: Pageable,
        filter: CourseBackOfficeFilters
    ): ResponseEntity<List<CourseLowData>> {
        val tutor = authentication.principal as UserPrincipal
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(backOfficeService.getMyCoursesData(tutor.id, pageable, filter))
    }

    @GetMapping("/courses/{courseId}")
    @PreAuthorize("hasRole('TUTOR')")
    fun getMyCourseIndividualData(
        authentication: Authentication,
        @PathVariable courseId: Long,
        filter: DurationFilter
    ): ResponseEntity<CourseLowData> {
        val tutor = authentication.principal as UserPrincipal
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(backOfficeService.getCourseData(courseId, tutor.id, filter))
    }

    @GetMapping("/courses/{courseId}/students")
    @PreAuthorize("hasRole('TUTOR')")
    fun getMyCourseStudentsData(
        @PathVariable courseId: Long,
        authentication: Authentication,
        pageable: Pageable,
        filter: StudentBackOfficeFilters
    ): ResponseEntity<List<StudentDataResponse>> {
        val tutor = authentication.principal as UserPrincipal
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(backOfficeService.getMyCourseStudentData(courseId, tutor.id, pageable, filter))
    }

    @GetMapping("/students")
    @PreAuthorize("hasRole('TUTOR')")
    fun getMyStudentsData(
        authentication: Authentication,
        pageable: Pageable,
        filter: StudentBackOfficeFilters
    ): ResponseEntity<List<StudentDataResponse>> {
        val tutor = authentication.principal as UserPrincipal
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(backOfficeService.getMyStudentsData(tutor.id, pageable, filter))
    }

    @GetMapping("/students/{studentId}")
    @PreAuthorize("hasRole('TUTOR')")
    fun getMyStudentIndividualInfo(
        @PathVariable studentId: Long,
        authentication: Authentication,
        filter: DurationFilter
    ): ResponseEntity<StudentDataResponse> {
        val tutor = authentication.principal as UserPrincipal
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(backOfficeService.getStudentData(tutor.id, studentId, filter))
    }

    @GetMapping("/myData")
    @PreAuthorize("hasRole('TUTOR')")
    fun getMyData(
        authentication: Authentication,
        filter: DurationFilter
    ) : ResponseEntity<TutorLowData> {
        val tutor = authentication.principal as UserPrincipal
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(backOfficeService.getMyData(tutor.id, filter))}

}