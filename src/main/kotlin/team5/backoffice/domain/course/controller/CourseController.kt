package team5.backoffice.domain.course.controller

import org.springframework.data.domain.Pageable
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import team5.backoffice.domain.course.dto.*
import team5.backoffice.domain.course.model.PageRequest
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
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(courseService.getAllCourses(cursor, pageSize, 1L)) //TODO(need to be changed after security implemented)
    }

    @GetMapping("/{courseId}")
    fun getCourseById(
        @PathVariable courseId: Long,
    ): ResponseEntity<CourseResponse> {
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(courseService.getCourseById(courseId, 1L)) //TODO(need to be changed after security implemented)
    }

    @GetMapping("/filter")
    fun getFilteredCourses(
        @ModelAttribute filter: FilteringRequest,
        pageable: Pageable
    ): ResponseEntity<List<CourseListResponse>> {
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(courseService.getFilteredCourses(filter, pageable, 1L))
    }

    @PostMapping()
    fun createCourse(
        @RequestBody request: CourseRequest
    ): ResponseEntity<CourseSimpleResponse> {
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(courseService.createCourse(request))
    }

    @PutMapping("/{courseId}")
    fun updateCourse(
        @PathVariable courseId: Long,
        @RequestBody request: CourseRequest
    ): ResponseEntity<CourseSimpleResponse> {
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(courseService.updateCourseById(courseId, request))

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

    // 북마크에 중복에 관한 로직은 프론트엔드가 나만 아니면 돼
    @DeleteMapping("/{courseId}/bookmark")
    fun undoBookmarkedCourse(
        @PathVariable courseId: Long,
    ): ResponseEntity<Unit> {
        //studentId =
        return ResponseEntity
            .status(HttpStatus.NO_CONTENT)
            .body(courseService.removeBookmark(courseId, 4L)) //TODO(need to be changed after security implemented)
    }

    @PostMapping("/{courseId}/subscription")
    fun subscribeCourse(
        @PathVariable courseId: Long,
    ): ResponseEntity<Unit> {
        // 중복결제 방지를 위한 id 찾아오기 studentId = content.subject
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(courseService.subscribe(courseId, 4L)) // TODO(need to be changed after security implemented)
    }
}