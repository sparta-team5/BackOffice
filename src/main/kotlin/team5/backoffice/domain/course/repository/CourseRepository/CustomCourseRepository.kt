package team5.backoffice.domain.course.repository.CourseRepository

import org.springframework.data.domain.Pageable
import team5.backoffice.domain.course.dto.CursorRequest
import team5.backoffice.domain.course.dto.FilteringRequest
import team5.backoffice.domain.course.model.Course
import team5.backoffice.domain.course.model.PageRequest

interface CustomCourseRepository {

    fun findAllCourses(cursor: CursorRequest, pageSize: Int): List<Course>

    fun findByFilter(filter: FilteringRequest, pageable: Pageable): List<Course>

}