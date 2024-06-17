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

    @GetMapping()
    fun getAllCourses(
        @ModelAttribute cursor: CursorRequest,
        @RequestParam pageSize: Int
    ): ResponseEntity<CursorPageResponse> {
        val studentId: Long? = null
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(courseService.getAllCourses(cursor, pageSize, studentId)) //TODO(need to be changed after security implemented)
    }

    @GetMapping("/{courseId}")
    fun getCourseById(
        @PathVariable courseId: Long,
    ): ResponseEntity<CourseResponse> {
        val studentId: Long? = null
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(courseService.getCourseById(courseId, studentId)) //TODO(need to be changed after security implemented)
    }

    @GetMapping("/filter")
    fun getFilteredCourses(
        @ModelAttribute filter: FilteringRequest,
        @ModelAttribute durationFilter: DurationFilter,
        pageable: Pageable
    ): ResponseEntity<List<CourseListResponse>> {
        val studentId: Long? = null
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(courseService.getFilteredCourses(filter, pageable, studentId, durationFilter))
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
    ): ResponseEntity<Unit> {
        // studentId =
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(courseService.addBookmark(courseId, 4L))//TODO(need to be changed after security implemented)
    }

    @DeleteMapping("/{courseId}/bookmark")
    fun undoBookmarkedCourse(
        @PathVariable courseId: Long,
    ): ResponseEntity<Unit> {
        return ResponseEntity
            .status(HttpStatus.NO_CONTENT)
            .body(courseService.removeBookmark(courseId, 4L)) //TODO(need to be changed after security implemented)
    }

    @PostMapping("/{courseId}/subscription")
    fun subscribeCourse(
        @PathVariable courseId: Long,
    ): ResponseEntity<Unit> {
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(courseService.subscribe(courseId, 4L)) // TODO(need to be changed after security implemented)
    }
}