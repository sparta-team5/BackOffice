package domain.course.controller

import domain.course.dto.*
import domain.course.service.CourseService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/courses")
class CourseController(
    private val courseService: CourseService
) {

    @GetMapping()
    fun getAllCourses(
        @ModelAttribute cursor: CursorDto
    ): ResponseEntity<CursorPageResponse> {
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(courseService.getAllCourses(cursor))
    }

    @GetMapping("/{courseId}")
    fun getCourseById(
        @PathVariable courseId: Long,
    ): ResponseEntity<CourseResponse> {
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(courseService.getCourseById(courseId))
    }

    @GetMapping("/filter") //(category=?title=?rate=?...)
    fun getFilteredCourses(
        @ModelAttribute cursorDto: CursorDto,
        @ModelAttribute filter: FilteringDto
    ): ResponseEntity<CursorPageResponse> {
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(courseService.getFilteredCourses(cursorDto, filter))
    }

    @PostMapping()
    fun createCourse(
        @RequestBody request: CourseRequest,
    ): ResponseEntity<CourseSimpleResponse> {
        //토큰에서 id 추출 후 넘겨주기? tutorId = ContextHolder.content.subject
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(courseService.createCourse(request, tutorId))
    }

    @PutMapping("/{courseId}")
    fun updateCourse(
        @PathVariable courseId: Long,
        @RequestBody request: CourseRequest,
    ): ResponseEntity<CourseSimpleResponse> {
        //토큰에서 id 추출 후 넘겨주기? tutorId = ContextHolder.content.subject
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(courseService.updateCourseById(courseId, request, tutorId))
    }

    @DeleteMapping("/{courseId}")
    fun deleteCourse(
        @PathVariable courseId: Long,
    ): ResponseEntity<Unit> {
        //토큰에서 id 추출 후 넘겨주기? tutorId = ContextHolder.content.subject
        courseService.deleteCourseById(courseId, tutorId)
        return ResponseEntity
            .status(HttpStatus.NO_CONTENT)
            .build()
    }

    @PostMapping("/{courseId}/bookmark")
    fun bookmarkCourse(
        @PathVariable courseId: Long,
    ): ResponseEntity<Unit> {
        // studentId =
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(courseService.addBookmark(courseId, studentId))
    }

    // 북마크에 중복에 관한 로직은 프론트엔드가 나만 아니면 돼
    @DeleteMapping("/{courseId}/bookmark")
    fun undoBookmarkedCourse(
        @PathVariable courseId: Long,
    ): ResponseEntity<Unit> {
        //studentId =
        return ResponseEntity
            .status(HttpStatus.NO_CONTENT)
            .body(courseService.removeBookmark(courseId, studentId))
    }

    @PostMapping("/{courseId}/subscribe")
    fun subscribeCourse(
        @PathVariable courseId: Long,
    ): ResponseEntity<Unit> {
        // 중복결제 방지를 위한 id 찾아오기 studentId = content.subject
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(courseService.subscribe(courseId, studentId))
    }
}