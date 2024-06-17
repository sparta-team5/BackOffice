package team5.backoffice.domain.course.repository.CourseRepository

import org.springframework.data.domain.Pageable
import team5.backoffice.domain.course.dto.*

interface CustomCourseRepository {

    fun getCourseAvgRate(courseId: Long) :Double

    fun findAllCourses(cursor: CursorRequest, pageSize: Int): List<CourseLowData>

    fun findByFilter(filter: FilteringRequest, pageable: Pageable, durationFilter: DurationFilter): List<CourseLowData>

    fun findCoursesInfoByTutor(tutorId: Long, pageable: Pageable, filter: FilteringRequest, durationFilter: DurationFilter): List<TutorLowData>

    fun getCourseViewSum(courseId: Long) : Long

}