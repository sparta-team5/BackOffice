package team5.backoffice.domain.course.service

import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Slice
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import team5.backoffice.domain.auth.dto.GetUserInfoRequest
import team5.backoffice.domain.auth.tutor.service.TutorService
import team5.backoffice.domain.course.dto.*
import team5.backoffice.domain.course.model.*
import team5.backoffice.domain.course.repository.BookmarkRepository
import team5.backoffice.domain.course.repository.CategoryRepository
import team5.backoffice.domain.course.repository.CourseRepository.CourseRepository
import team5.backoffice.domain.course.repository.SubscriptionRepository
import team5.backoffice.domain.user.model.Tutor
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
) {

    fun getAllCourses(cursor: CursorRequest, pageable: Pageable, studentId: Long?): CursorPageResponse {
        val courses = if (studentId != null) {
            courseRepository.findAllCourses(cursor, pageable)
                .map {
                    CourseListResponse.from(
                        it,
                        isBookmarkExists(it.id!!, studentId),
                        isSubscribeExists(it.id, studentId)
                    )
                }
        } else {
            courseRepository.findAllCourses(cursor, pageable)
                .map {
                    CourseListResponse.from(
                        it,
                        isBookmarked = false,
                        isSubscribed = false
                    )
                }
        }
        val nextCursor = when(cursor.cursorOrderType) {
            OrderType.createdAt -> courses.lastOrNull()?.createdTime ?: LocalDateTime.now()
            OrderType.viewCount -> courses.lastOrNull()?.viewCount ?: Long.MAX_VALUE
        }
        return CursorPageResponse(courses, nextCursor)
    }

    fun getCourseById(courseId: Long, studentId: Long?): CourseResponse {
        val course = courseRepository.findByIdOrNull(courseId) ?: throw RuntimeException("Course not found")
        course.increaseViewCount()
        return if (studentId != null) {
            CourseResponse.from(course, isBookmarkExists(courseId, studentId), isSubscribeExists(courseId, studentId))
        } else {
            CourseResponse.from(course, isBookMarked = false, isSubscribed = false)
        }
    }

    fun createCourse(request: CourseRequest): CourseSimpleResponse {
        val category = categoryRepository.findByName(request.category) ?: throw RuntimeException("Category not found")
        val tutor = tutorRepository.findByIdOrNull(request.tutorId) ?: throw RuntimeException("Tid not found")
        return courseRepository.save(
            Course(
                title = request.title,
                tutor = tutor,
                description = request.description,
                category = category,
                imageUrl = request.imageUrl,
                viewCount = 0,
                createdTime = LocalDateTime.now()
            )
        ).let { CourseSimpleResponse.from(it) }
    }

    @Transactional
    fun updateCourseById(courseId: Long, request: CourseRequest): CourseSimpleResponse {
        val course = courseRepository.findByIdOrNull(courseId) ?: throw RuntimeException("Course not found")
        val category = categoryRepository.findByName(request.category) ?: throw RuntimeException("Category not found")
        if (course.tutor.id != request.tutorId) throw RuntimeException("Unauthorized tutor")
        course.apply {
            this.title = request.title
            this.description = request.description
            this.category = category
            this.imageUrl = request.imageUrl
        }
        return CourseSimpleResponse.from(course)
    }

    fun deleteCourseById(courseId: Long) {
        val course = courseRepository.findByIdOrNull(courseId) ?: throw RuntimeException("Course not found")
        courseRepository.delete(course)
    }

    fun addBookmark(courseId: Long, studentId: Long) {
        courseRepository.findByIdOrNull(courseId) ?: throw RuntimeException("Course not found")
        studentRepository.findByIdOrNull(studentId) ?: throw RuntimeException("Student not found")
        if (!isBookmarkExists(courseId, studentId)) {
            bookmarkRepository.save(Bookmark(BookmarkId(courseId, studentId)))
        }
    }

    fun removeBookmark(courseId: Long, studentId: Long) {
        courseRepository.findByIdOrNull(courseId) ?: throw RuntimeException("Course not found")
        studentRepository.findByIdOrNull(studentId) ?: throw RuntimeException("Student not found")
        val bookmark = bookmarkRepository.findByIdOrNull(BookmarkId(courseId, studentId))
            ?: throw RuntimeException("Bookmark not found")
        bookmarkRepository.delete(bookmark)
    }

    fun subscribe(courseId: Long, studentId: Long) {
        courseRepository.findByIdOrNull(courseId) ?: throw RuntimeException("Course not found")
        studentRepository.findByIdOrNull(studentId) ?: throw RuntimeException("Student not found")
        if (!isSubscribeExists(courseId, studentId)) {
            subscriptionRepository.save(Subscription(SubscriptionId(courseId, studentId)))
        }
    }

    fun isBookmarkExists(courseId: Long, studentId: Long): Boolean {
        return bookmarkRepository.existsById(BookmarkId(courseId, studentId))
    }

    fun isSubscribeExists(courseId: Long, studentId: Long): Boolean {
        return subscriptionRepository.existsById(SubscriptionId(courseId, studentId))
    }

    fun sliceToListResponse(courseSlice: Slice<Course>, studentId: Long?): List<CourseListResponse> {
        return if (studentId != null) courseSlice.content.map {
            CourseListResponse.from(
                it,
                isBookmarkExists(it.id!!, studentId),
                isSubscribeExists(it.id, studentId)
            )
        }
        else courseSlice.content.map { CourseListResponse.from(it, isBookmarked = false, isSubscribed = false) }

    }


    fun checkValidate(token: String): Tutor {
        return tutorService.getTutorInfo(
            GetUserInfoRequest(token = token)
        )
    }
}