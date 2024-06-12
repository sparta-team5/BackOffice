package domain.lecture.repository

import domain.lecture.model.Lecture
import org.springframework.data.jpa.repository.JpaRepository

interface LectureRepository : JpaRepository<Lecture, Long> {
    fun findByIdAndCourseId(id: Long, courseId: Long): Lecture?
}