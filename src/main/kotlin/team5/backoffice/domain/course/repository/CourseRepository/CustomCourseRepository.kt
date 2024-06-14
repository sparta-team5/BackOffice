package team5.backoffice.domain.course.repository.CourseRepository

import org.springframework.data.domain.Pageable
import team5.backoffice.domain.course.dto.CursorRequest
import team5.backoffice.domain.course.model.Course

interface CustomCourseRepository {

    fun findAllCourses(cursor: CursorRequest, pageable: Pageable): List<Course>

}