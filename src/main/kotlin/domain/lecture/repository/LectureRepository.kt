package domain.lecture.repository

interface LectureRepository {
    fun findByCourseIdAndId(courseId: Long, id: Long) : Unit
}