package team5.backoffice.domain.course.repository.CourseRepository

import com.querydsl.core.BooleanBuilder
import com.querydsl.jpa.impl.JPAQuery
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Repository
import team5.backoffice.domain.course.dto.CursorRequest
import team5.backoffice.domain.course.model.Course
import team5.backoffice.domain.course.model.OrderType
import team5.backoffice.domain.course.model.QCourse
import team5.backoffice.infra.querydsl.QueryDslSupport

@Repository
class CourseRepositoryImpl : CustomCourseRepository, QueryDslSupport() {

    private val course = QCourse.course

    override fun findAllCourses(cursor: CursorRequest, pageable: Pageable): List<Course> {
        val query = queryFactory.selectFrom(course)
            .where(applyCursorPosition(cursor))
            .applyOrderBy(cursor)
            .limit(pageable.pageSize.toLong())
            .fetch()

        return query
    }

    private fun applyCursorPosition(cursor: CursorRequest): BooleanBuilder {
        val builder = BooleanBuilder()
        if (cursor.cursorOrderType == OrderType.createdAt) {
            cursor.cursorTime?.let {
                builder.and(course.createdTime.before(it))
            }
        } else if (cursor.cursorOrderType == OrderType.viewCount) {
            cursor.cursorView?.let {
                builder.and(course.viewCount.lt(it))
            }
        }
        return builder
    }

    private fun JPAQuery<Course>.applyOrderBy(cursor: CursorRequest): JPAQuery<Course> {
        return when (cursor.cursorOrderType) {
            OrderType.createdAt -> this.orderBy(course.createdTime.desc())
            OrderType.viewCount -> this.orderBy(course.viewCount.desc())
        }
    }
}

