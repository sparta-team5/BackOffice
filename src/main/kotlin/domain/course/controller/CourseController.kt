package domain.course.controller

import domain.course.dto.CourseListResponse
import domain.course.dto.CourseResponse
import domain.course.dto.CursorDto
import domain.course.dto.FilteringDto
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
    fun getAllCourses () : ResponseEntity<List<CourseListResponse>> {
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(courseService.getAllCourses())
    }

    @GetMapping("/{courseId}")
    fun getCourseById(
        @PathVariable courseId : Long,
    ) : ResponseEntity<CourseResponse> {
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(courseService.getCourseById(courseId))
    }

    @GetMapping("")
    fun getFilteredCourses(
        @ModelAttribute cursorDto: CursorDto,
        @ModelAttribute filter: FilteringDto
    ) : ResponseEntity<List<CourseListResponse>> {
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(courseService.getFilteredCourses(cursorDto, filter))
    }

    @PostMapping()
    fun createCourse() : ResponseEntity<CourseResponse> {
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(courseService.createCourse())
    }

    @PutMapping("/{courseId}")
    fun updateCourse(
        @PathVariable courseId: Long,
    ) : ResponseEntity<CourseResponse> {
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(courseService.updateCourseById())
    }

    @DeleteMapping("/{courseId}")
    fun deleteCourse(
        @PathVariable courseId: Long,
    ) : ResponseEntity<CourseResponse> {
        courseService.deleteCourseById(courseId)
        return ResponseEntity
            .status(HttpStatus.NO_CONTENT)
            .build()
    }

    @PostMapping("/{courseId}/bookmark")
    fun bookmarkCourse(
        @PathVariable courseId: Long,
    ) : ResponseEntity<CourseResponse> {
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(courseService.addBookmark(courseId))
    }

    @DeleteMapping("/{courseId}/bookmark")
    fun undoBookmarkedCourse(
        @PathVariable courseId: Long,
    ) : ResponseEntity<CourseResponse>{
        return ResponseEntity
            .status(HttpStatus.NO_CONTENT)
            .body(courseService.removeBookmark(courseId))
    }

    @PostMapping("/{courseId}/subscribe")
    fun subscribeCourse(
        @PathVariable courseId: Long,
    ) : ResponseEntity<CourseResponse> {
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(courseService.subscribe(courseId))
    }
}