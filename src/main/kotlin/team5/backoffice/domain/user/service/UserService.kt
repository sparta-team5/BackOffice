package team5.backoffice.domain.user.service

import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import team5.backoffice.domain.user.dto.*
import team5.backoffice.domain.user.model.Follow
import team5.backoffice.domain.user.model.FollowId
import team5.backoffice.domain.user.repository.FollowRepository
import team5.backoffice.domain.user.repository.StudentRepository
import team5.backoffice.domain.user.repository.TutorRepository

@Service
class UserService(
    private val studentRepository: StudentRepository,
    private val tutorRepository: TutorRepository,
    private val followRepository: FollowRepository
) {
    fun getStudentById(studentId: Long): StudentResponse {
        val student = studentRepository.findByIdOrNull(studentId) ?: throw RuntimeException("Student")
        return StudentResponse.from(student)
    }

    @Transactional
    fun updateStudentById(studentId: Long, request: UpdateStudentRequest): StudentResponse {
        val student = studentRepository.findByIdOrNull(studentId) ?: throw RuntimeException("Student")
        student.apply {
            this.nickname = request.nickname
        }
        return studentRepository.save(student).let { StudentResponse.from(student) }
    }

    fun deleteStudentById(studentId: Long) {
        val student = studentRepository.findByIdOrNull(studentId) ?: throw RuntimeException("Student")
        studentRepository.delete(student)
    }


    fun getTutorById(tutorId: Long): TutorResponse {
        val tutor = tutorRepository.findByIdOrNull(tutorId) ?: throw RuntimeException("Tutor")
        return TutorResponse.from(tutor)
    }

    fun updateTutorById(tutorId: Long, request: UpdateTutorRequest): TutorResponse {
        val tutor = tutorRepository.findByIdOrNull(tutorId) ?: throw RuntimeException("Tutor")

        tutor.apply {
            nickname = request.nickname
            description = request.description
            career = request.career
        }
        return tutorRepository.save(tutor).let { TutorResponse.from(tutor) }
    }

    fun deleteTutorById(tutorId: Long) {
        val tutor = tutorRepository.findByIdOrNull(tutorId) ?: throw RuntimeException("Tutor")
        tutorRepository.delete(tutor)
    }

    fun followStudentAndTutor(tutorId: Long, studentId: Long): FollowResponse {
        val tutor = tutorRepository.findByIdOrNull(tutorId) ?: throw RuntimeException("Tutor")
        if (followRepository.existsById(
                FollowId(
                    studentId,
                    tutor.id!!
                )
            )
        ) throw IllegalStateException("already followed")
        return followRepository.save(Follow(FollowId(studentId, tutor.id!!)))
            .let { FollowResponse.from(it) }

    }

    fun unfollowStudentAndTutor(tutorId: Long, studentId: Long) {
        val tutor = tutorRepository.findByIdOrNull(tutorId) ?: throw RuntimeException("Tutor")
        val follow = followRepository.findByIdOrNull(FollowId(studentId, tutor.id!!))
            ?: throw RuntimeException("Follow not found")
        followRepository.delete(follow)
    }

}