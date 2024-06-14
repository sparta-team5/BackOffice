package team5.backoffice.domain.course.service

import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Slice
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import team5.backoffice.domain.course.dto.*
import team5.backoffice.domain.course.model.*
import team5.backoffice.domain.course.repository.BookmarkRepository
import team5.backoffice.domain.course.repository.CategoryRepository
import team5.backoffice.domain.course.repository.CourseRepository
import team5.backoffice.domain.course.repository.SubscriptionRepository
import team5.backoffice.domain.user.repository.StudentRepository
import team5.backoffice.domain.user.repository.TutorRepository

@Service
class CourseService(
    private val courseRepository: CourseRepository,
    private val studentRepository: StudentRepository,
    private val subscriptionRepository: SubscriptionRepository,
    private val bookmarkRepository: BookmarkRepository,
    private val categoryRepository: CategoryRepository,
    private val tutorRepository: TutorRepository,
) {

    fun getAllCourses(cursor: CursorRequest, studentId: Long?): CursorPageResponse {
        val pageable = PageRequest.of(0, cursor.page)
        val courseSlice: Slice<Course> = courseRepository.findAllCourse(cursor.cursor, pageable, cursor.orderBy)
        val nextCursor = if (courseSlice.hasNext()) courseSlice.nextPageable().pageNumber else null
        val pageResponse = sliceToListResponse(courseSlice, studentId)
        return CursorPageResponse(pageResponse, nextCursor)
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
//
//    fun getFilteredCourses(cursor: CursorRequest, filter: FilteringRequest, studentId: Long?): CursorPageResponse {
//        val pageable = PageRequest.of(0, cursor.page)
//        val courseSlice: Slice<Course> =
//            courseRepository.findAllByCursorAndFilter(cursor.cursor, pageable, cursor.orderBy, filter) // 동적쿼리필요
//        val nextCursor: Int? = courseSlice.nextPageable().pageNumber ?: null
//        val pageResponse = sliceToListResponse(courseSlice, studentId)
//        return CursorPageResponse(pageResponse, nextCursor)
//    }

    fun createCourse(request: CourseRequest, tutorId: Long): CourseSimpleResponse {
        val category = categoryRepository.findByName(request.category) ?: throw RuntimeException("Category not found")
        val tutor = tutorRepository.findByIdOrNull(tutorId) ?: throw RuntimeException("Tid not found")
        return courseRepository.save(
            Course(
                title = request.title,
                tutor = tutor,
                description = request.description,
                category = category,
                imageUrl = request.imageUrl,
                viewCount = 0,
            )
        ).let { CourseSimpleResponse.from(it) }
    }

    @Transactional
    fun updateCourseById(courseId: Long, request: CourseRequest, tutorId: Long): CourseSimpleResponse {
        val course = courseRepository.findByIdOrNull(courseId) ?: throw RuntimeException("Course not found")
        val category = categoryRepository.findByName(request.category) ?: throw RuntimeException("Category not found")
        if (course.tutor.id != tutorId) throw RuntimeException("Unauthorized tutor")
        course.apply {
            this.title = request.title
            this.description = request.description
            this.category = category
            this.imageUrl = request.imageUrl
        }
        return CourseSimpleResponse.from(course)
    }

    fun deleteCourseById(courseId: Long, tutorId: Long) {
        val course = courseRepository.findByIdOrNull(courseId) ?: throw RuntimeException("Course not found")
        if (course.tutor.id != tutorId) throw RuntimeException("Unauthorized tutor")
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
}