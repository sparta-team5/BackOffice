package team5.backoffice.domain.course.controller

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

    @GetMapping()
    fun getAllCourses(
        @ModelAttribute cursor: CursorRequest,
    ): ResponseEntity<CursorPageResponse> {
        // val studentId : Long? = 있으면 받고 없으면 null
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(courseService.getAllCourses(cursor, 1L)) //TODO(need to be changed after security implemented)
    }

    @GetMapping("/{courseId}")
    fun getCourseById(
        @PathVariable courseId: Long,
    ): ResponseEntity<CourseResponse> {
        // val studentId : Long? = 있으면 받고 없으면 null
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(courseService.getCourseById(courseId, 1L)) //TODO(need to be changed after security implemented)
    }

//    @GetMapping("/filter") //(category=?title=?rate=?...)
//    fun getFilteredCourses(
//        @ModelAttribute cursorRequest: CursorRequest,
//        @ModelAttribute filter: FilteringRequest
//    ): ResponseEntity<CursorPageResponse> {
//        // val studentId : Long? = 있으면 받고 없으면 null
//        return ResponseEntity
//            .status(HttpStatus.OK)
//            .body(courseService.getFilteredCourses(cursorRequest, filter, 1L))
//    }

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