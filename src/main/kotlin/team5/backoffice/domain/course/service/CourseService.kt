package team5.backoffice.domain.course.service

import org.springframework.data.domain.Pageable
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import team5.backoffice.domain.auth.tutor.service.TutorService
import team5.backoffice.domain.course.dto.*
import team5.backoffice.domain.course.model.*
import team5.backoffice.domain.course.repository.BookmarkRepository
import team5.backoffice.domain.course.repository.CategoryRepository
import team5.backoffice.domain.course.repository.CourseRepository.CourseRepository
import team5.backoffice.domain.course.repository.SubscriptionRepository
import team5.backoffice.domain.exception.ModelNotFoundException
import team5.backoffice.domain.exception.UnauthorizedUserException
import team5.backoffice.domain.review.repository.ReviewRepository
import team5.backoffice.domain.user.repository.StudentRepository
import team5.backoffice.domain.user.repository.TutorRepository
import java.time.LocalDateTime

@Service
class CourseService(
    private val courseRepository: CourseRepository,
    private val studentRepository: StudentRepository,
    private val tutorService: TutorService,
    private val subscriptionRepository: SubscriptionRepository,
    private val bookmarkRepository: BookmarkRepository,
    private val categoryRepository: CategoryRepository,
    private val tutorRepository: TutorRepository,
    private val reviewRepository: ReviewRepository,
) {

    fun getAllCourses(cursor: CursorRequest, pageSize: Int, studentId: Long?): CursorPageResponse {
        val courses = coursesToListResponse(courseRepository.findAllCourses(cursor, pageSize), studentId)
        val nextCursor = when (cursor.cursorOrderType) {
            OrderType.createdAt -> courses.lastOrNull()?.createdAt ?: LocalDateTime.now()
            OrderType.viewCount -> courses.lastOrNull()?.viewCount ?: Long.MAX_VALUE
            else -> null
        }
        return CursorPageResponse(courses, nextCursor)
    }

    fun getCourseById(courseId: Long, studentId: Long?): CourseResponse {
        val course =
            courseRepository.findByIdOrNull(courseId) ?: throw ModelNotFoundException("course", "id: $courseId")
        return if (studentId != null) {
            CourseResponse.from(
                course,
                isBookmarkExists(courseId, studentId),
                isSubscribeExists(courseId, studentId),
                getCourseAverageRate(courseId)
            )
        } else {
            CourseResponse.from(course, isBookMarked = false, isSubscribed = false, getCourseAverageRate(courseId))
        }
    }

    fun getFilteredCourses(
        filter: FilteringRequest,
        pageable: Pageable,
        studentId: Long?,
        durationFilter: DurationFilter
    ): List<CourseListResponse> {
        val courses = courseRepository.findByFilter(filter, pageable, durationFilter)
        return coursesToListResponse(courses, studentId)
    }

    @Transactional
    fun createCourse(request: CourseRequest, tutorId: Long): CourseSimpleResponse {
        val category = categoryRepository.findByName(request.category) ?: throw ModelNotFoundException(
            "category",
            "name: ${request.category}"
        )
        val tutor = tutorRepository.findByIdOrNull(tutorId) ?: throw ModelNotFoundException("tutor", "id: $tutorId")
        return courseRepository.save(
            Course(
                title = request.title,
                tutor = tutor,
                description = request.description,
                category = category,
                imageUrl = request.imageUrl,
                createdAt = LocalDateTime.now(),
            )
        ).let { CourseSimpleResponse.from(it) }
    }

    @Transactional
    fun updateCourseById(courseId: Long, request: CourseRequest, tutorId: Long): CourseSimpleResponse {
        val course =
            courseRepository.findByIdOrNull(courseId) ?: throw ModelNotFoundException("course", "id: $courseId")
        val category = categoryRepository.findByName(request.category) ?: throw ModelNotFoundException(
            "category",
            "name: ${request.category}"
        )
        if (course.tutor.id != tutorId) throw UnauthorizedUserException()
        course.apply {
            this.title = request.title
            this.description = request.description
//            this.category = category
            this.imageUrl = request.imageUrl
        }
        return CourseSimpleResponse.from(course)
    }

    fun deleteCourseById(courseId: Long) {
        val course = courseRepository.findByIdOrNull(courseId) ?: throw RuntimeException("Course not found")

        @Transactional
        fun deleteCourseById(courseId: Long, tutorId: Long) {
            val course =
                courseRepository.findByIdOrNull(courseId) ?: throw ModelNotFoundException(
                    "course",
                    "id: $courseId"
                )
            if (course.tutor.id != tutorId) throw UnauthorizedUserException()
            courseRepository.delete(course)
        }

        @Transactional
        fun addBookmark(courseId: Long, studentId: Long) {
            courseRepository.findByIdOrNull(courseId) ?: throw ModelNotFoundException("course", "id: $courseId")
            studentRepository.findByIdOrNull(studentId) ?: throw ModelNotFoundException(
                "student",
                "id: $studentId"
            )
            if (!isBookmarkExists(courseId, studentId)) {
                bookmarkRepository.save(Bookmark(BookmarkId(courseId, studentId), LocalDateTime.now()))
            }
        }
    }

    @Transactional
    fun removeBookmark(courseId: Long, studentId: Long) {
        courseRepository.findByIdOrNull(courseId) ?: throw ModelNotFoundException("course", "id: $courseId")
        studentRepository.findByIdOrNull(studentId) ?: throw ModelNotFoundException(
            "student",
            "id: $studentId"
        )
        val bookmark = bookmarkRepository.findByIdOrNull(BookmarkId(courseId, studentId))
            ?: throw ModelNotFoundException("bookmark", "id")
        bookmarkRepository.delete(bookmark)
    }

    @Transactional
    fun subscribe(courseId: Long, studentId: Long) {
        courseRepository.findByIdOrNull(courseId) ?: throw ModelNotFoundException("course", "id: $courseId")
        studentRepository.findByIdOrNull(studentId) ?: throw ModelNotFoundException(
            "student",
            "id: $studentId"
        )
        if (!isSubscribeExists(courseId, studentId)) {
            subscriptionRepository.save(Subscription(SubscriptionId(courseId, studentId), LocalDateTime.now()))
        }
    }

    fun isBookmarkExists(courseId: Long, studentId: Long): Boolean {
        return bookmarkRepository.existsById(BookmarkId(courseId, studentId))
    }

    fun isSubscribeExists(courseId: Long, studentId: Long): Boolean {
        return subscriptionRepository.existsById(SubscriptionId(courseId, studentId))
    }

    fun coursesToListResponse(courses: List<CourseLowData>, studentId: Long?): List<CourseListResponse> {
        return if (studentId != null) courses.map {
            CourseListResponse.from(
                it,
                isBookmarkExists(it.id, studentId),
                isSubscribeExists(it.id, studentId),
                getCourseAverageRate(it.id)
            )
        }
        else courses.map {
            CourseListResponse.from(
                it,
                isBookmarked = false,
                isSubscribed = false,
                getCourseAverageRate(it.id!!)
            )
        }
    }

    fun getCourseAverageRate(courseId: Long): Double {
        return courseRepository.getCourseAvgRate(courseId)
    }
}


