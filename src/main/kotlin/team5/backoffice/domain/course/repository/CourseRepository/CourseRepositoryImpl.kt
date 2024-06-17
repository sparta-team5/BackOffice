package team5.backoffice.domain.course.repository.CourseRepository

import com.querydsl.core.BooleanBuilder
import com.querydsl.core.Tuple
import com.querydsl.core.types.Projections
import com.querydsl.jpa.impl.JPAQuery
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Repository
import team5.backoffice.domain.course.dto.*
import team5.backoffice.domain.course.model.*
import team5.backoffice.domain.review.model.QReview
import team5.backoffice.domain.user.model.*
import team5.backoffice.domain.user.model.QTutor.tutor
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
            .from(course)
            .join(course, review.course)
            .where(course.id.eq(courseId))
            .groupBy(course)
            .fetchOne() ?: 0.0
    }

    override fun getCourseViewSum(courseId: Long): Long {
        return queryFactory.select(view.count())
            .from(course)
//            .join(course, view.courses)
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
                course.tutor,
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
            .where(applyCursorPosition(cursor))
            .groupBy(
                course.id,
                course.title,
                course.description,
                course.tutor,
                category.name,
                course.imageUrl,
                course.createdAt
            )
            .applyOrderBy(cursor.cursorOrderType)
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

        filter.title?.let { builder.and(course.title.like("%$it%")) }
        filter.description?.let { builder.and(course.description.like("%$it%")) }
        filter.tutorNickName?.let { builder.and(course.tutor.nickname.eq(it)) }
        filter.category?.let { builder.and(category.name.eq(it)) }
        filter.viewCount?.let { havingBuilder.and(view.count().goe(it)) }
        filter.rate?.let { havingBuilder.and(review.rate.avg().goe(it)) }
        filter.bookmarkCount?.let { havingBuilder.and(bookmark.count().goe(it)) }
        filter.subscriptionCount?.let { havingBuilder.and(subscription.count().goe(it)) }
        durationFilter.duration?.let { builder.and(view.createdAt.after(it)) }
        durationFilter.duration?.let { builder.and(bookmark.createdAt.after(it)) }
        durationFilter.duration?.let { builder.and(subscription.createdAt.after(it)) }
        durationFilter.duration?.let { builder.and(review.createdAt.after(it)) }

        val query = queryFactory
            .select(
                Projections.constructor(
                    CourseLowData::class.java,
                    course.id,
                    course.title,
                    course.description,
                    course.tutor,
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
            .groupBy(course)
            .having(havingBuilder)
            .applyOrderBy(filter.orderType)
            .offset(pageable.offset)
            .limit(pageable.pageSize.toLong())
            .fetch()


        return query
    }

    override fun findCoursesInfoByTutor(
        tutorId: Long,
        pageable: Pageable,
        filter: FilteringRequest,
        durationFilter: DurationFilter
    ): List<TutorLowData> {
        val builder = BooleanBuilder()

        filter.title?.let { builder.and(course.title.like("%$it%")) }
        filter.description?.let { builder.and(course.description.like("%$it%")) }
        filter.tutorNickName?.let { builder.and(course.tutor.nickname.eq(it)) }
        filter.category?.let { builder.and(category.name.eq(it)) }
        filter.viewCount?.let { builder.and(view.count().goe(it)) }
        filter.rate?.let { builder.and(review.rate.avg().goe(it)) }
        filter.bookmarkCount?.let { builder.and(bookmark.count().goe(it)) }
        filter.subscriptionCount?.let { builder.and(subscription.count().goe(it)) }
        durationFilter.duration?.let { builder.and(view.createdAt.after(it)) }
        durationFilter.duration?.let { builder.and(bookmark.createdAt.after(it)) }
        durationFilter.duration?.let { builder.and(subscription.createdAt.after(it)) }
        durationFilter.duration?.let { builder.and(review.createdAt.after(it)) }

        val query = queryFactory
            .select(
                Projections.constructor(
                    TutorLowData::class.java,
                    view.count(),
                    course.count(),
                    subscription.count(),
                    bookmark.count(),
                    follow.count(),
                    review.rate.avg(),
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
            .groupBy(course)
            .offset(pageable.offset)
            .limit(pageable.pageSize.toLong())
            .fetch()

        return query
    }

//    fun findMyStudentInfo(
//        tutorId: Long,
//        pageable: Pageable,
//        studentFilter: SomethingFilter,
//        groupType: String,
//        durationFilter: DurationFilter
//    ): List<Student> {
//        val builder = BooleanBuilder()
//        val student = QStudent.student
//
//        //TODO: 필터
//
//        val query = queryFactory.select(student)
//            .from(student)
//            .leftJoin(follow).on(follow.student.id.eq(student.id))
//            .leftJoin(tutor).on(follow.tutor.id.eq(tutor.id))
//            .leftJoin(student).on(student.id.eq(view.student.id))
//            .where(tutor.id.eq(tutorId))
//            .applyGroupBy(groupType)
//            .offset(pageable.offset)
//            .limit(pageable.pageSize.toLong())
//            .fetch()
//        return query
//    }

//    // 데이터별 학생 수를 구하기 위한 groupBy 지정 함수 이 함수 사용시 student.count()가 반드시 필요함
//    private fun <T> JPAQuery<T>.applyGroupBy(groupType: String): JPAQuery<T> {
//        return when (groupType) {
//            "age" -> this.groupBy(student.age)
//            "local" -> this.groupBy(student.local)
//            "student" -> this.groupBy(student)
//            else -> throw RuntimeException("Illegal group type: $groupType")
//        }
//    }

    //나를 팔로우한 학생이 내 코스를 얼마나 구독했는가
//    fun findMyStudentWhoSubscribeTutor(tutorId: Long): List<Tuple> {
//
//
//        val query = queryFactory.select(student, student.count())
//            .from(student)
//            .join(follow).on(student.eq(follow.student))
//            .join(subscription).on(student.eq(subscription.student))
//            .where(follow.tutor.id.eq(tutorId))
//            .groupBy(student) // 학생별로 몇개의 코스를 구독했는가
//            .fetch()
//        return query
//    }

//     아직 student 에 필드가 없어 단순 객체 반환으로 구현해 봤습니다.
//
//    duration filter 사용시 해당 타입의 지정 시간 이후의 데이터만 구할 수 있습니다
}

