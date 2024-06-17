package team5.backoffice.domain.course.controller

import org.springframework.data.domain.Pageable
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import team5.backoffice.domain.course.dto.*
import team5.backoffice.domain.course.service.CourseService

@RestController
@RequestMapping("/courses")
class CourseController(
    private val courseService: CourseService,
) {

    @GetMapping("/all")
    fun getAllCoursesWithoutAuth(
        @ModelAttribute cursor: CursorRequest,
    ): ResponseEntity<CursorPageResponse> {
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(courseService.getAllCourses(cursor, null))
    }

    @GetMapping
    fun getAllCourses(
        @ModelAttribute cursor: CursorRequest,
        authentication: Authentication,
    ): ResponseEntity<CursorPageResponse> {
        val student = authentication.principal as UserPrincipal
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(courseService.getAllCourses(cursor, student.id))
    }

    @GetMapping("/{courseId}/all")
    fun getCourseByIdWithoutAuth(
        @PathVariable courseId: Long
    ): ResponseEntity<CourseResponse> {
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(courseService.getCourseById(courseId, null))
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
        pageable: Pageable
    ): ResponseEntity<List<CourseListResponse>> {
        val student = authentication.principal as UserPrincipal
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(courseService.getCourseById(courseId, student.id))
    }


    @PostMapping()
    fun createCourse(
        @RequestBody request: CourseRequest
    ): ResponseEntity<CourseSimpleResponse> {
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(courseService.createCourse(request, 1L))
    }

    @PutMapping("/{courseId}")
    fun updateCourse(
        @PathVariable courseId: Long,
        @RequestBody request: CourseRequest
    ): ResponseEntity<CourseSimpleResponse> {
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(courseService.updateCourseById(courseId, request, 1L))
    }

    @DeleteMapping("/{courseId}")
    fun deleteCourse(
        @PathVariable courseId: Long
    ): ResponseEntity<Unit> {
        return ResponseEntity
            .status(HttpStatus.NO_CONTENT)
            .body(courseService.deleteCourseById(courseId))
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