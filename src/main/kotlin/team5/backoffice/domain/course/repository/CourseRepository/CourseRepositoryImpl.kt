package team5.backoffice.domain.course.repository.CourseRepository

import com.querydsl.core.BooleanBuilder
import com.querydsl.core.types.Projections
import com.querydsl.jpa.impl.JPAQuery
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Repository
import team5.backoffice.domain.backoffice.dto.CourseBackOfficeFilters
import team5.backoffice.domain.backoffice.dto.StudentBackOfficeFilters
import team5.backoffice.domain.backoffice.dto.StudentData
import team5.backoffice.domain.backoffice.dto.TutorLowData
import team5.backoffice.domain.course.dto.CourseLowData
import team5.backoffice.domain.course.dto.CursorRequest
import team5.backoffice.domain.course.dto.DurationFilter
import team5.backoffice.domain.course.dto.FilteringRequest
import team5.backoffice.domain.course.model.*
import team5.backoffice.domain.review.model.QReview
import team5.backoffice.domain.user.model.QFollow
import team5.backoffice.domain.user.model.QStudent
import team5.backoffice.domain.user.model.QTutor
import team5.backoffice.infra.querydsl.QueryDslSupport

@Repository
class CourseRepositoryImpl : CustomCourseRepository, QueryDslSupport() {

    private val course = QCourse.course
    private val review = QReview.review
    private val bookmark = QBookmark.bookmark
    private val subscription = QSubscription.subscription
    private val category = QCategory.category
    private val view = QView.view
    private val follow = QFollow.follow
    private val student = QStudent.student
    private val tutor = QTutor.tutor

    override fun getCourseAvgRate(courseId: Long): Double {
        return queryFactory.select(review.rate.avg())
            .from(review)
            .join(course).on(course.eq(review.course))
            .where(course.id.eq(courseId))
            .groupBy(course)
            .fetchOne() ?: 0.0
    }

    override fun getCourseViewSum(courseId: Long): Long {
        return queryFactory.select(view.count())
            .from(view)
            .join(course).on(course.eq(view.course))
            .where(course.id.eq(courseId))
            .groupBy(course)
            .fetchOne() ?: 0
    }

    override fun findAllCourses(cursor: CursorRequest, pageSize: Int): List<CourseLowData> {
        val query = queryFactory.select(
            Projections.constructor(
                CourseLowData::class.java,
                course.id,
                course.title,
                course.description,
                course.tutor.nickname,
                category.name,
                course.imageUrl,
                course.createdAt,
                review.rate.avg(),
                view.count(),
                bookmark.count(),
                subscription.count(),
            )
        )
            .from(course)
            .leftJoin(view).on(course.eq(view.course))
            .leftJoin(review).on(course.eq(review.course))
            .leftJoin(bookmark).on(course.eq(bookmark.course))
            .leftJoin(subscription).on(course.eq(subscription.course))
            .groupBy(
                course.id,
                course.title,
                course.description,
                course.tutor.nickname,
                category.name,
                course.imageUrl,
                course.createdAt,
                review.rate.avg(),
                view.count(),
                bookmark.count(),
                subscription.count(),
            )
            .having(applyCursorPosition(cursor))
            .applyOrderBy(cursor.cursorOrderType)
            .orderBy(course.id.desc())
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
                builder.and(view.count().lt(it))
            }
        }
        return builder
    }

    private fun <T> JPAQuery<T>.applyOrderBy(orderType: OrderType): JPAQuery<T> {
        return when (orderType) {
            OrderType.createdAt -> this.orderBy(course.createdAt.desc())
            OrderType.viewCount -> this.orderBy(view.count().desc())
            OrderType.rate -> this.orderBy(review.rate.avg().desc())
            OrderType.bookmark -> this.orderBy(bookmark.count().desc())
            OrderType.subscription -> this.orderBy(subscription.count().desc())
            OrderType.follow -> this.orderBy(follow.count().desc())
        }
    }


    override fun findByFilter(
        filter: FilteringRequest,
        pageable: Pageable,
        durationFilter: DurationFilter
    ): List<CourseLowData> {
        val builder = BooleanBuilder()
        val havingBuilder = BooleanBuilder()

        filter.word?.let {
            val searchWord = "%$it%"
            builder.and(
                course.title.like(searchWord)
                    .or(course.description.like(searchWord))
                    .or(course.tutor.nickname.like(searchWord))
            )
        }
        filter.category?.let { builder.and(category.name.contains(it)) }
        filter.viewCount?.let { havingBuilder.and(view.count().goe(it)) }
        filter.rate?.let { havingBuilder.and(review.rate.avg().goe(it)) }
        filter.bookmarkCount?.let { havingBuilder.and(bookmark.count().goe(it)) }
        filter.subscriptionCount?.let { havingBuilder.and(subscription.count().goe(it)) }
        durationFilter.duration?.let { builder.and(course.createdAt.after(it)) }

        val query = queryFactory
            .select(
                Projections.constructor(
                    CourseLowData::class.java,
                    course.id,
                    course.title,
                    course.description,
                    course.tutor.nickname,
                    category.name,
                    course.imageUrl,
                    course.createdAt,
                    review.rate.avg(),
                    view.count(),
                    bookmark.count(),
                    subscription.count(),
                )
            )
            .from(course)
            .leftJoin(review).on(course.eq(review.course))
            .leftJoin(view).on(course.eq(view.course))
            .leftJoin(bookmark).on(course.eq(bookmark.course))
            .leftJoin(subscription).on(course.eq(subscription.course))
            .where(builder)
            .groupBy(
                course.id,
                course.title,
                course.description,
                course.tutor.nickname,
                category.name,
                course.imageUrl,
                course.createdAt,
            )
            .having(havingBuilder)
            .applyOrderBy(filter.orderType)
            .orderBy(course.id.desc())
            .offset(pageable.offset)
            .limit(pageable.pageSize.toLong())
            .fetch()
        return query
    }

    override fun findTutorData(
        tutorId: Long,
        filter: DurationFilter
    ): TutorLowData? {
        val builder = BooleanBuilder()

        filter.duration?.let {
            builder.or(view.createdAt.after(it))
            builder.or(bookmark.createdAt.after(it))
            builder.or(subscription.createdAt.after(it))
            builder.or(review.createdAt.after(it))
        }

        val query = queryFactory
            .select(
                Projections.constructor(
                    TutorLowData::class.java,
                    view.count(),
                    course.count(),
                    subscription.count(),
                    bookmark.count(),
                    follow.count(),
                    review.rate.avg()
                )
            )
            .from(course)
            .leftJoin(tutor).on(course.tutor.eq(tutor))
            .leftJoin(review).on(course.eq(review.course))
            .leftJoin(view).on(course.eq(view.course))
            .leftJoin(subscription).on(course.eq(subscription.course))
            .leftJoin(bookmark).on(course.eq(bookmark.course))
            .leftJoin(follow).on(tutor.eq(follow.tutor))
            .where(
                course.tutor.id.eq(tutorId)
                    .and(builder)
            )
            .fetchOne()

        return query
    }

//    // 데이터별 학생 수를 구하기 위한 groupBy 지정 함수 이 함수 사용시 student.count()가 반드시 필요함
//    private fun <T> JPAQuery<T>.applyGroupBy(groupType: String): JPAQuery<T> {
//        return when (groupType) {
//            "age" -> this.groupBy(student.age)
//            "local" -> this.groupBy(student.local)
//            "student" -> this.groupBy(student)
//            else -> throw RuntimeException("Illegal group type: $groupType")
//        }
//    }


    override fun findMyCoursesData(
        tutorId: Long?,
        pageable: Pageable,
        filter: CourseBackOfficeFilters
    ): List<CourseLowData> {
        val builder = BooleanBuilder()
        val havingBuilder = BooleanBuilder()

        filter.duration?.let {
            builder.or(view.createdAt.after(it))
            builder.or(bookmark.createdAt.after(it))
            builder.or(subscription.createdAt.after(it))
            builder.or(review.createdAt.after(it))
        }
        filter.viewCount?.let { havingBuilder.and(view.count().goe(it)) }
        filter.bookingCount?.let { havingBuilder.and(bookmark.count().goe(it)) }
        filter.subscriptionCount?.let { havingBuilder.and(subscription.count().goe(it)) }
        filter.rateLimit?.let { havingBuilder.and(review.rate.avg().goe(it)) }


        val query = queryFactory
            .select(
                Projections.constructor(
                    CourseLowData::class.java,
                    course.id,
                    course.title,
                    course.description,
                    course.tutor.nickname,
                    course.category.name,
                    course.imageUrl,
                    course.createdAt,
                    review.rate.avg(),
                    view.count(),
                    bookmark.count(),
                    subscription.count()
                )
            )
            .from(course)
            .leftJoin(review).on(course.eq(review.course))
            .leftJoin(view).on(course.eq(view.course))
            .leftJoin(bookmark).on(course.eq(bookmark.course))
            .leftJoin(subscription).on(course.eq(subscription.course))
            .where(
                course.tutor.id.eq(tutorId)
                    .and(builder)
            )
            .groupBy(
                course.id,
                course.title,
                course.description,
                course.tutor.nickname,
                category.name,
                course.imageUrl,
                course.createdAt,
            )
            .having(havingBuilder)
            .applyOrderBy(filter.orderType)
            .orderBy(course.id.desc())
            .offset(pageable.offset)
            .limit(pageable.pageSize.toLong())
            .fetch()
        return query
    }

    override fun findMyCourse(courseId: Long, filter: DurationFilter): CourseLowData? {
        val builder = BooleanBuilder()

        filter.duration?.let {
            builder.or(view.createdAt.after(it))
            builder.or(bookmark.createdAt.after(it))
            builder.or(subscription.createdAt.after(it))
            builder.or(review.createdAt.after(it))
        }

        val query = queryFactory
            .select(
                Projections.constructor(
                    CourseLowData::class.java,
                    course.id,
                    course.title,
                    course.description,
                    course.tutor.nickname,
                    course.category.name,
                    course.imageUrl,
                    course.createdAt,
                    review.rate.avg(),
                    view.count(),
                    bookmark.count(),
                    subscription.count()
                )
            )
            .from(course)
            .leftJoin(review).on(course.eq(review.course))
            .leftJoin(view).on(course.eq(view.course))
            .leftJoin(bookmark).on(course.eq(bookmark.course))
            .leftJoin(subscription).on(course.eq(subscription.course))
            .where(course.id.eq(courseId))
            .groupBy(
                course.id,
                course.title,
                course.description,
                course.tutor.nickname,
                course.category.name,
                course.imageUrl,
                course.createdAt
            )
            .having(builder)
            .fetchOne()
        return query
    }

    override fun findMyCourseStudentData(
        courseId: Long,
        filter: StudentBackOfficeFilters,
        pageable: Pageable
    ): List<StudentData> {
        val builder = BooleanBuilder()
        val havingBuilder = BooleanBuilder()

        filter.duration?.let {
            builder.or(view.createdAt.after(it))
            builder.or(bookmark.createdAt.after(it))
            builder.or(subscription.createdAt.after(it))
        }
        filter.viewCount?.let { havingBuilder.and(view.count().goe(it)) }
        filter.bookingCount?.let { havingBuilder.and(bookmark.count().goe(it)) }
        filter.subscriptionCount?.let { havingBuilder.and(subscription.count().goe(it)) }
        filter.rateLimit?.let { havingBuilder.and(review.rate.avg().goe(it)) }

        val query = queryFactory
            .select(
                Projections.constructor(
                    StudentData::class.java,
                    student.id,
                    student.nickname,
                    view.count(),
                    bookmark.count(),
                    subscription.count(),
                    review.rate.avg(),
                )
            )
            .from(student)
            .leftJoin(view).on(student.eq(view.student))
            .leftJoin(subscription).on(student.eq(subscription.student))
            .leftJoin(bookmark).on(student.eq(bookmark.student))
            .leftJoin(review).on(student.eq(review.student))
            .leftJoin(follow).on(student.eq(follow.student))
            .where(
                subscription.course.id.eq(courseId)
                    .and(builder)
            )
            .groupBy(
                student.id,
                student.nickname
            )
            .having(havingBuilder)
            .applyOrderBy(filter.orderType)
            .orderBy(student.id.desc())
            .fetch()
        return query
    }

    override fun findMyStudentsData(
        tutorId: Long?,
        filter: StudentBackOfficeFilters,
        pageable: Pageable
    ): List<StudentData> {
        val builder = BooleanBuilder()
        val havingBuilder = BooleanBuilder()

        filter.duration?.let { builder.or(view.createdAt.goe(it)) }
        filter.duration?.let { builder.or(bookmark.createdAt.goe(it)) }
        filter.duration?.let { builder.or(subscription.createdAt.goe(it)) }
        filter.viewCount?.let { havingBuilder.and(view.count().goe(it)) }
        filter.bookingCount?.let { havingBuilder.and(bookmark.count().goe(it)) }
        filter.subscriptionCount?.let { havingBuilder.and(subscription.count().goe(it)) }
        filter.rateLimit?.let { havingBuilder.and(review.rate.avg().goe(it)) }

        val query = queryFactory
            .select(
                Projections.constructor(
                    StudentData::class.java,
                    student.id,
                    student.nickname,
                    view.count(),
                    bookmark.count(),
                    subscription.count(),
                    review.rate.avg(),
                )
            )
            .from(student)
            .leftJoin(follow).on(student.eq(follow.student))
            .leftJoin(subscription).on(student.eq(subscription.student))
            .leftJoin(bookmark).on(student.eq(bookmark.student))
            .leftJoin(view).on(student.eq(view.student))
            .leftJoin(review).on(student.eq(review.student))
            .where(
                follow.tutor.id.eq(tutorId)
                    .and(builder)
            )
            .groupBy(
                student.id,
                student.nickname
            )
            .having(builder)
            .having(havingBuilder)
            .applyOrderBy(filter.orderType)
            .orderBy(student.id.desc())
            .offset(pageable.offset)
            .limit(pageable.pageSize.toLong())
            .fetch()
        return query
    }

    override fun findStudentData(studentId: Long, filter: DurationFilter): StudentData? {
        val builder = BooleanBuilder()

        filter.duration?.let {
            builder.or(view.createdAt.after(it))
            builder.or(bookmark.createdAt.after(it))
            builder.or(subscription.createdAt.after(it))
        }

        val query = queryFactory.select(
            Projections.constructor(
                StudentData::class.java,
                student.id,
                student.nickname,
                view.count(),
                bookmark.count(),
                subscription.count(),
                review.rate.avg(),
            )
        )
            .from(student)
            .leftJoin(follow).on(student.eq(follow.student))
            .leftJoin(subscription).on(student.eq(subscription.student))
            .leftJoin(bookmark).on(student.eq(bookmark.student))
            .leftJoin(view).on(student.eq(view.student))
            .leftJoin(review).on(student.eq(review.student))
            .where(
                student.id.eq(studentId)
//                    .or(builder)
            )
            .groupBy(
                student.id,
                student.nickname,
            )
            .limit(1)
            .fetchOne()
        return query
    }
}

