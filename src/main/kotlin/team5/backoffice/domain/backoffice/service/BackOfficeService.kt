package team5.backoffice.domain.backoffice.service

import org.springframework.data.domain.Pageable
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import team5.backoffice.domain.backoffice.dto.*
import team5.backoffice.domain.course.dto.CourseLowData
import team5.backoffice.domain.course.dto.DurationFilter
import team5.backoffice.domain.course.repository.CourseRepository.CourseRepository
import team5.backoffice.domain.exception.InvalidCredentialException
import team5.backoffice.domain.exception.ModelNotFoundException
import team5.backoffice.domain.user.model.FollowId
import team5.backoffice.domain.user.repository.FollowRepository
import team5.backoffice.domain.user.repository.StudentRepository
import team5.backoffice.domain.user.repository.TutorRepository

@Service
class BackOfficeService(
    private val courseRepository: CourseRepository,
    private val followRepository: FollowRepository,
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
    ): List<StudentDataResponse> {
        val course = courseRepository.findByIdOrNull(courseId) ?: throw ModelNotFoundException("course", "$courseId")
        if (course.tutor.id != tutorId) throw InvalidCredentialException()
        val student = courseRepository.findMyCourseStudentData(courseId, filter, pageable)
        return student.map { dataToResponse(it, tutorId)}
    }

    fun getMyStudentsData(tutorId: Long?, pageable: Pageable, filter: StudentBackOfficeFilters): List<StudentDataResponse> {
        val student = courseRepository.findMyStudentsData(tutorId, filter, pageable)
        return student.map { dataToResponse(it, tutorId) }
    }

    fun getStudentData(tutorId: Long?,studentId: Long, filter: DurationFilter): StudentDataResponse {
        val student = courseRepository.findStudentData(studentId, filter) ?: throw ModelNotFoundException(
            "student",
            "$studentId"
        )
        return dataToResponse(student, tutorId)
    }

    fun getMyData(tutorId: Long, filter: DurationFilter): TutorLowData {
        return courseRepository.findTutorData(tutorId, filter) ?: throw ModelNotFoundException("tutor", "$tutorId")
    }

    private fun isMyFollower(studentId: Long, tutorId: Long): Boolean {
        return followRepository.existsById(FollowId(studentId, tutorId))
    }

    private fun dataToResponse(student: StudentData, tutorId: Long?): StudentDataResponse {
        return StudentDataResponse.from(student, isMyFollower(student.studentId, tutorId!!))
    }
}