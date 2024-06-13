package team5.backoffice.domain.lecture.service

import jakarta.transaction.Transactional
import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.stereotype.Service
import team5.backoffice.domain.course.repository.CourseRepository
import team5.backoffice.domain.lecture.dto.CreateLectureRequest
import team5.backoffice.domain.lecture.dto.LectureResponse
import team5.backoffice.domain.lecture.dto.UpdateLectureRequest
import team5.backoffice.domain.lecture.model.Lecture
import team5.backoffice.domain.lecture.repository.LectureRepository

@Service
class LectureService(
    private val lectureRepository: LectureRepository,
    private val courseRepository: CourseRepository,
) {
    fun getLecture(courseId: Long, lectureId: Long): LectureResponse {
        // TODO 해당 lecture가 연관된 course에 등록된 사용자인지 확인절차 필요
        val lecture = lectureRepository.findByIdAndCourseId(lectureId, courseId) ?: throw RuntimeException()
        return LectureResponse.from(lecture)
    }

    @PreAuthorize("hasRole('ROLE_TUTOR')")
    fun addLecture(courseId: Long, request: CreateLectureRequest): LectureResponse {
        val course = courseRepository.findByIdOrNull(courseId) ?: throw RuntimeException()
        // TODO( 요청한 사람이 course 주인인지 확인하기)
        return Lecture(
            title = request.title,
            videoUrl = request.videoUrl,
            course = course
        ).let { lectureRepository.save(it) }
            .let { LectureResponse.from(it) }
    }

    @PreAuthorize("hasRole('ROLE_TUTOR')")
    @Transactional
    fun updateLecture(courseId: Long, lectureId: Long, request: UpdateLectureRequest): LectureResponse {
        val course = courseRepository.findByIdOrNull(courseId) ?: throw RuntimeException()
        val lecture = lectureRepository.findByIdAndCourseId(lectureId, courseId) ?: throw RuntimeException()
        lecture.apply {
            this.title = request.title
            this.videoUrl = request.videoUrl
            this.course = course
        }
        return LectureResponse.from(lecture)

    }

    @PreAuthorize("hasRole('ROLE_TUTOR')")
    fun deleteLecture(courseId: Long, lectureId: Long): Unit {
        // TODO( 요청한 사람이 course 주인인지 확인하기)
        val lecture = lectureRepository.findByIdAndCourseId(lectureId, courseId) ?: throw RuntimeException()
        lectureRepository.delete(lecture)
    }

    fun getAllLecture(courseId: Long): List<LectureResponse> {
        courseRepository.findByIdOrNull(courseId) ?: throw RuntimeException()
        return lectureRepository.findAllByCourseId(courseId)
            .map { LectureResponse.from(it) }
    }

}