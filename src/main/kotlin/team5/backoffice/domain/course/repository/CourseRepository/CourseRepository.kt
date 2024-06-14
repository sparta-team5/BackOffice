package team5.backoffice.domain.course.repository.CourseRepository

import org.springframework.data.jpa.repository.JpaRepository
import team5.backoffice.domain.course.model.Course

interface CourseRepository : JpaRepository<Course, Long>, CustomCourseRepository {
}