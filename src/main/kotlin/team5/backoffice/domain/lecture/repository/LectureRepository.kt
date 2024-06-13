package team5.backoffice.domain.lecture.repository

import org.springframework.data.jpa.repository.JpaRepository
import team5.backoffice.domain.lecture.model.Lecture

interface LectureRepository : JpaRepository<Lecture, Long> {
    fun findByIdAndCourseId(id: Long, courseId: Long): Lecture?
    fun findAllByCourseId(courseId: Long): List<Lecture>
}