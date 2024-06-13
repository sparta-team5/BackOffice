package team5.backoffice.domain.lecture.repository

import team5.backoffice.domain.lecture.model.Lecture
import org.springframework.data.jpa.repository.JpaRepository

interface LectureRepository : JpaRepository<Lecture, Long> {
    fun findByIdAndCourseId(id: Long, courseId: Long): Lecture?
}