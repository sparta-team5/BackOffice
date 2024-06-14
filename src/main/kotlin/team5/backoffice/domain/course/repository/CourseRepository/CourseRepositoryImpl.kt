package team5.backoffice.domain.course.repository.CourseRepository

import com.querydsl.core.BooleanBuilder
import com.querydsl.jpa.impl.JPAQuery
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Repository
import team5.backoffice.domain.course.dto.CursorRequest
import team5.backoffice.domain.course.dto.FilteringRequest
import team5.backoffice.domain.course.model.*
import team5.backoffice.domain.user.model.QTutor
import team5.backoffice.infra.querydsl.QueryDslSupport

@Repository
class CourseRepositoryImpl : CustomCourseRepository, QueryDslSupport() {

    private val course = QCourse.course

    override fun findAllCourses(cursor: CursorRequest, pageSize: Int): List<Course> {
        val query = queryFactory.selectFrom(course)
            .where(applyCursorPosition(cursor))
            .applyOrderBy(cursor)
            .limit(pageSize.toLong())
            .fetch()

        return query
    }


    private fun applyCursorPosition(cursor: CursorRequest): BooleanBuilder {
        val builder = BooleanBuilder()
        if (cursor.cursorOrderType == OrderType.createdAt) {
            cursor.cursorTime?.let {
                builder.and(course.createdAt.before(it))
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
            OrderType.createdAt -> this.orderBy(course.createdAt.desc())
            OrderType.viewCount -> this.orderBy(course.viewCount.desc())
        }
    }

    override fun findByFilter(filter: FilteringRequest, pageable: Pageable): List<Course> {
        val builder = BooleanBuilder()
        val tutor = QTutor.tutor
        val category = QCategory.category

        filter.title?.let { builder.and(course.title.like(it)) }
        filter.description?.let { builder.and(course.description.like(it)) }
        filter.tutorNickName?.let { builder.and(course.tutor.nickname.eq(it)) }
//        filter.category?.let { builder.and(course.category.eq(it)) }

        val query = queryFactory.selectFrom(course)
            .where(builder)
            .orderBy(course.createdAt.desc())
            .offset(pageable.offset)
            .limit(pageable.pageSize.toLong())
            .fetch()

        return query

    }
}

