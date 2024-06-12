package domain.course.service

import domain.course.dto.*
import domain.course.model.Course
import domain.course.model.toListResponse
import domain.course.model.toResponse
import domain.course.model.toSimpleResponse
import domain.course.repository.CourseRepository
import domain.user.repository.StudentRepository
import domain.user.repository.TutorRepository
import org.springframework.data.domain.*
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Service
class CourseService(
    private val courseRepository: CourseRepository,
    private val studentRepository: StudentRepository,
    private val tutorRepository: TutorRepository,
) {

    fun getAllCourses(cursor: CursorRequest, studentId: Long?): CursorPageResponse {
        val pageable = PageRequest.of(0, cursor.page)
        val courseSlice: Slice<Course> = courseRepository.findAllCourse(cursor.cursor, pageable, cursor.orderBy)
        val nextCursor = if (courseSlice.hasNext()) courseSlice.nextPageable().pageNumber else null

        val pageResponse = if (studentId != null) {
            courseSlice.content.map {
                CourseListResponse.from(it, existBookmark(studentId, it.id!!), existSubscribe(studentId, it.id))
            }
        } else {
            courseSlice.content.map {
                CourseListResponse.from(it, isBookmarked = false, isSubscribed = false)
            }
        }
        return CursorPageResponse(pageResponse, nextCursor)
    }

    fun getCourseById(courseId: Long, studentId: Long?): CourseResponse {
        val course = courseRepository.findByIdOrNull(courseId)
            ?: throw RuntimeException("Course not found")
        course.viewCount++
        return if (studentId != null) {
            CourseResponse.from(course, existBookmark(studentId, courseId), existSubscribe(studentId, courseId))
        } else {
            CourseResponse.from(course, false, false)
        }
    }

    fun getFilteredCourses(cursor: CursorRequest, filter: FilteringRequest, studentId: Long?): CursorPageResponse {
        val pageable = PageRequest.of(0, cursor.page)
        val courseSlice: Slice<Course> =
            courseRepository.findAllByCursorAndFilter(cursor.cursor, pageable, cursor.orderBy, filter) // 동적쿼리필요
        val nextCursor: Int? = courseSlice.nextPageable().pageNumber ?: null

        val pageResponse = if (studentId != null) {
            courseSlice.content.map {
                CourseListResponse.from(it, existBookmark(studentId, it.id!!), existSubscribe(studentId, it.id))
            }
        } else {
            courseSlice.content.map {
                CourseListResponse.from(it, isBookmarked = false, isSubscribed = false)
            }
        }
        return CursorPageResponse(pageResponse, nextCursor)
    }


    fun createCourse(request: CourseRequest, tutorId: Long): CourseSimpleResponse {
        val tutor = tutorRepository.findByIdOrNull(tutorId)
            ?: throw RuntimeException("Tutor not found")

        val course = courseRepository.save(
            Course(
                title = request.title,
                tutor = tutor,
                description = request.description,
                category = request.category,
                imageUrl = request.imageUrl,
                viewCount = 0,
                rate = 0.0,
                createdAt = LocalDateTime.now()
            )
        )
        return CourseSimpleResponse.from(course)
    }

    @Transactional
    fun updateCourseById(courseId: Long, request: CourseRequest, tutorId: Long): CourseSimpleResponse {
        val course = courseRepository.findByIdOrNull(courseId)
            ?: throw RuntimeException("Course not found")
        if (tutorId != course.tutor.id) {
            throw RuntimeException("No Permission")
        }
        course.apply {
            title = request.title
            description = request.description
            category = request.category
            imageUrl = request.imageUrl
        }
        println("Course ${course.id} has been successfully updated")
        return CourseSimpleResponse.from(course)
    }

    fun deleteCourseById(courseId: Long, tutorId: Long) {
        val course = courseRepository.findByIdOrNull(courseId)
            ?: throw RuntimeException("Course not found")
        if (tutorId != course.tutor.id) {
            throw RuntimeException("No Permission")
        }
        courseRepository.delete(course)
        println("Course ${course.id} has been successfully deleted")
    }

    // ERD 찾아보니 따로 bookmark 에 id가 존재하지 않아서 이런 로직을 짜봤습니다
    fun addBookmark(courseId: Long, studentId: Long) {
        val course = courseRepository.findByIdOrNull(courseId)
            ?: throw RuntimeException("Course not found")
        val student = studentRepository.findByIdOrNull(studentId)
            ?: throw RuntimeException("Student not found")
        student.apply {
            student.bookmark.add(course)
        }
        studentRepository.save(student)
        println("Course ${course.id} is now marked")
    }

    fun removeBookmark(courseId: Long, studentId: Long) {
        val course = courseRepository.findByIdOrNull(courseId)
            ?: throw RuntimeException("Course not found")
        val student = studentRepository.findByIdOrNull(studentId)
            ?: throw RuntimeException("Student not found")
        student.apply {
            student.bookmark.remove(course)
        }
        studentRepository.save(student)
        println("Course ${course.id} is now unmarked")
    }

    fun subscribe(courseId: Long, studentId: Long) {
        val course = courseRepository.findByIdOrNull(courseId)
            ?: throw RuntimeException("Course not found")
        val student = studentRepository.findByIdOrNull(studentId)
            ?: throw RuntimeException("Student not found")
        if (!existSubscribe(studentId, courseId)) {
            throw RuntimeException("Already subscribed")
        }
        student.apply {
            student.subscription.add(course)
        }
        println("Course ${course.id} is now subscribed")
    }

    private fun existBookmark(studentId: Long, courseId: Long): Boolean {
        // TODO : bookmark 에 student 와 course 를 가진 개체가 있는지 확인
    }

    private fun existSubscribe(studentId: Long, courseId: Long): Boolean {
        // TODO : subscription 에 student 와 course 를 가진 개체가 있는지 확인
    }
}