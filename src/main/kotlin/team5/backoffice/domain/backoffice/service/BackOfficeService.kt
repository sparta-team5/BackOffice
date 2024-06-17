package team5.backoffice.domain.backoffice.service

import org.springframework.data.domain.Pageable
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import team5.backoffice.domain.backoffice.dto.CourseBackOfficeFilters
import team5.backoffice.domain.backoffice.dto.StudentBackOfficeFilters
import team5.backoffice.domain.backoffice.dto.StudentData
import team5.backoffice.domain.backoffice.dto.TutorLowData
import team5.backoffice.domain.course.dto.CourseLowData
import team5.backoffice.domain.course.dto.DurationFilter
import team5.backoffice.domain.course.repository.CourseRepository.CourseRepository
import team5.backoffice.domain.exception.InvalidCredentialException
import team5.backoffice.domain.exception.ModelNotFoundException
import team5.backoffice.domain.user.repository.StudentRepository
import team5.backoffice.domain.user.repository.TutorRepository

@Service
class BackOfficeService(
    private val courseRepository: CourseRepository,
    private val studentRepository: StudentRepository,
    private val tutorRepository: TutorRepository,
) {
    fun getMyCoursesData(tutorId: Long?, pageable: Pageable, filter: CourseBackOfficeFilters): List<CourseLowData> {
        return courseRepository.findMyCoursesData(tutorId, pageable, filter)
    }

    fun getCourseData(courseId: Long, tutorId: Long?, filter: DurationFilter): CourseLowData {
        val course = courseRepository.findByIdOrNull(courseId) ?: throw ModelNotFoundException("course", "$courseId")
        val courseData =
            courseRepository.findMyCourse(courseId, filter) ?: throw ModelNotFoundException("course", "$courseId")
        if (course.tutor.id != tutorId) throw InvalidCredentialException()
        return courseData
    }

    fun getMyCourseStudentData(
        courseId: Long,
        tutorId: Long?,
        pageable: Pageable,
        filter: StudentBackOfficeFilters
    ): List<StudentData> {
        val course = courseRepository.findByIdOrNull(courseId) ?: throw ModelNotFoundException("course", "$courseId")
        if (course.tutor.id != tutorId) throw InvalidCredentialException()
        return courseRepository.findMyCourseStudentData(courseId, filter, pageable)
    }

    fun getMyStudentsData(tutorId: Long?, pageable: Pageable, filter: StudentBackOfficeFilters): List<StudentData> {
        return courseRepository.findMyStudentsData(tutorId, filter, pageable)
    }

    fun getStudentData(studentId: Long, filter: DurationFilter): StudentData {
        return courseRepository.findStudentData(studentId, filter) ?: throw ModelNotFoundException(
            "student",
            "$studentId"
        )
    }

    fun getMyData(tutorId: Long, filter: DurationFilter): TutorLowData {
        return courseRepository.findTutorData(tutorId, filter) ?: throw ModelNotFoundException("tutor", "$tutorId")
    }
}