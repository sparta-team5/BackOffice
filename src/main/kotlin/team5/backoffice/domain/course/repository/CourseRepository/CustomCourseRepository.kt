package team5.backoffice.domain.course.repository.CourseRepository

import org.springframework.data.domain.Pageable
import team5.backoffice.domain.backoffice.dto.CourseBackOfficeFilters
import team5.backoffice.domain.backoffice.dto.StudentBackOfficeFilters
import team5.backoffice.domain.backoffice.dto.StudentData
import team5.backoffice.domain.backoffice.dto.TutorLowData
import team5.backoffice.domain.course.dto.CourseLowData
import team5.backoffice.domain.course.dto.CursorRequest
import team5.backoffice.domain.course.dto.DurationFilter
import team5.backoffice.domain.course.dto.FilteringRequest

interface CustomCourseRepository {

    fun getCourseAvgRate(courseId: Long) :Double

    fun findAllCourses(cursor: CursorRequest, pageSize: Int): List<CourseLowData>

    fun findByFilter(filter: FilteringRequest, pageable: Pageable, durationFilter: DurationFilter): List<CourseLowData>

    fun findTutorData(tutorId: Long, filter: DurationFilter): TutorLowData?

    fun getCourseViewSum(courseId: Long) : Long

    fun findMyCoursesData(tutorId: Long?, pageable: Pageable, filter: CourseBackOfficeFilters): List<CourseLowData>

    fun findMyCourse(courseId: Long, filter: DurationFilter) : CourseLowData?

    fun findMyCourseStudentData(courseId: Long, filter: StudentBackOfficeFilters, pageable: Pageable) : List<StudentData>

    fun findMyStudentsData(tutorId: Long?, filter: StudentBackOfficeFilters, pageable: Pageable) : List<StudentData>

    fun findStudentData(studentId: Long, filter: DurationFilter) : StudentData?
}