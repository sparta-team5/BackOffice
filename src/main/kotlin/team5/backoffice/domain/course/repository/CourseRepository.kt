package team5.backoffice.domain.course.repository

import team5.backoffice.domain.course.model.Course
import team5.backoffice.domain.course.model.SortType
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Slice
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface CourseRepository : JpaRepository<Course, Long> {

    @Query("select c from Course c where (:cursor is null or :sortBy > :cursor) order by :sortBy desc")
    fun findAllCourse(cursor: Int, pageable: Pageable, sortBy: SortType): Slice<Course>
}