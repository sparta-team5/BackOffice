package team5.backoffice.domain.course.controller

import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.*
import team5.backoffice.domain.course.dto.*
import team5.backoffice.domain.course.service.CourseService
import team5.backoffice.infra.security.UserPrincipal

@RestController
@RequestMapping("/courses")
class CourseController(
    private val courseService: CourseService,
) {

    @GetMapping("/all")
    fun getAllCoursesWithoutAuth(
        @ModelAttribute cursor: CursorRequest,
        @RequestParam pageSize: Int
    ): ResponseEntity<CursorPageResponse> {
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(courseService.getAllCourses(cursor, pageSize, null))
    }

    @GetMapping
    fun getAllCourses(
        @ModelAttribute cursor: CursorRequest,
        @PageableDefault pageSize: Int,
        authentication: Authentication,
    ): ResponseEntity<CursorPageResponse> {
        val student = authentication.principal as UserPrincipal
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(courseService.getAllCourses(cursor, pageSize,student.id))
    }

    @GetMapping("/{courseId}/all")
    fun getCourseByIdWithoutAuth(
        @PathVariable courseId: Long,
        authentication: Authentication,
    ): ResponseEntity<CourseResponse> {
        val student = authentication.principal as UserPrincipal
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(courseService.getCourseById(courseId, student.id))
    }

    @GetMapping("/{courseId}")
    fun getCourseById(
        @PathVariable courseId: Long,
        authentication: Authentication
    ): ResponseEntity<CourseResponse> {
        val studentId: Long? = null
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(
                courseService.getCourseById(
                    courseId,
                    studentId
                )
            ) //TODO(need to be changed after security implemented)
    }

    @GetMapping("/filter")
    fun getFilteredCourses(
        @ModelAttribute filter: FilteringRequest,
        @ModelAttribute durationFilter: DurationFilter,
        authentication: Authentication,
        pageable: Pageable
    ): ResponseEntity<List<CourseListResponse>> {
        val student = authentication.principal as UserPrincipal
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(courseService.getFilteredCourses(filter, pageable, student.id, durationFilter))
    }


    @PostMapping()
    @PreAuthorize("hasRole('TUTOR')")
    fun createCourse(
        @RequestBody request: CourseRequest,
        authentication: Authentication,
    ): ResponseEntity<CourseSimpleResponse> {
        val tutor = authentication.principal as UserPrincipal
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(courseService.createCourse(request, tutor.id))
    }

    @PutMapping("/{courseId}")
    @PreAuthorize("hasRole('TUTOR')")
    fun updateCourse(
        @PathVariable courseId: Long,
        @RequestBody request: CourseRequest,
        authentication: Authentication,
    ): ResponseEntity<CourseSimpleResponse> {
        val tutor = authentication.principal as UserPrincipal
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(courseService.updateCourseById(courseId, request, tutor.id))

    }

    @DeleteMapping("/{courseId}")
    @PreAuthorize("hasRole('TUTOR')")
    fun deleteCourse(
        @PathVariable courseId: Long,
        authentication: Authentication,
    ): ResponseEntity<Unit> {
        val tutor = authentication.principal as UserPrincipal
        return ResponseEntity
            .status(HttpStatus.NO_CONTENT)
            .body(courseService.deleteCourseById(courseId, tutor.id))
    }

    @PostMapping("/{courseId}/bookmark")
    fun bookmarkCourse(
        @PathVariable courseId: Long,
        authentication: Authentication
    ): ResponseEntity<Unit> {
        val student = authentication.principal as UserPrincipal
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(courseService.addBookmark(courseId, student.id))
    }


    @DeleteMapping("/{courseId}/bookmark")
    fun undoBookmarkedCourse(
        @PathVariable courseId: Long,
        authentication: Authentication
    ): ResponseEntity<Unit> {
        val student = authentication.principal as UserPrincipal
        return ResponseEntity
            .status(HttpStatus.NO_CONTENT)
            .body(courseService.removeBookmark(courseId, student.id))
    }

    @PostMapping("/{courseId}/subscription")
    fun subscribeCourse(
        @PathVariable courseId: Long,
        authentication: Authentication
    ): ResponseEntity<Unit> {
        val student = authentication.principal as UserPrincipal
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(courseService.subscribe(courseId, student.id))
    }
}