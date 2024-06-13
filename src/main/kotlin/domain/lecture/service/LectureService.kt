package domain.lecture.service

import domain.course.repository.CourseRepository
import domain.lecture.dto.CreateLectureRequest
import domain.lecture.dto.LectureResponse
import domain.lecture.dto.UpdateLectureRequest
import domain.lecture.model.Lecture
import domain.lecture.repository.LectureRepository
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service

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

    fun addLecture(courseId: Long, request: CreateLectureRequest): LectureResponse {
        //val course = courseRepository.findByIdOrNull(courseId) ?: throw RuntimeException()
        // TODO( 요청한 사람이 course 주인인지 확인하기)
        return Lecture(
            title = request.title,
            videoUrl = request.videoUrl,
            courseId = courseId
        ).let { lectureRepository.save(it) }
            .let { LectureResponse.from(it) }
    }

    @Transactional
    fun updateLecture(courseId: Long, lectureId: Long, request: UpdateLectureRequest): LectureResponse {
        val lecture = lectureRepository.findByIdAndCourseId(lectureId, courseId) ?: throw RuntimeException()
        // TODO( 요청한 사람이 course 주인인지 확인하기)
        lecture.title = request.title
        lecture.videoUrl = request.videoUrl
        lecture.courseId = request.courseId
        return LectureResponse.from(lecture)

    }

    fun deleteLecture(courseId: Long, lectureId: Long): Unit {
        // TODO( 요청한 사람이 course 주인인지 확인하기)
        val lecture = lectureRepository.findByIdAndCourseId(lectureId, courseId) ?: throw RuntimeException()
        lectureRepository.delete(lecture)
    }

}