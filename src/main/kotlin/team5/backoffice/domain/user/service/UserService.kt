package team5.backoffice.domain.user.service

import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import team5.backoffice.domain.auth.oauth.OAuthLoginUserInfo
import team5.backoffice.domain.exception.ModelNotFoundException
import team5.backoffice.domain.user.dto.*
import team5.backoffice.domain.user.model.Follow
import team5.backoffice.domain.user.model.FollowId
import team5.backoffice.domain.user.model.Student
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
        val student =
            studentRepository.findByIdOrNull(studentId) ?: throw ModelNotFoundException("student", "id: $studentId")
        return StudentResponse.from(student)
    }

    @Transactional
    fun updateStudentById(studentId: Long, request: UpdateStudentRequest): StudentResponse {
        val student =
            studentRepository.findByIdOrNull(studentId) ?: throw ModelNotFoundException("student", "id: $studentId")
        student.apply {
            this.nickname = request.nickname
            this.introduction = request.introduction
        }
        return studentRepository.save(student).let { StudentResponse.from(student) }
    }

    @Transactional
    fun deleteStudentById(studentId: Long) {
        val student =
            studentRepository.findByIdOrNull(studentId) ?: throw ModelNotFoundException("student", "id: $studentId")
        studentRepository.delete(student)
    }


    fun getTutorById(tutorId: Long): TutorResponse {
        val tutor = tutorRepository.findByIdOrNull(tutorId) ?: throw ModelNotFoundException("tutor", "id: $tutorId")
        return TutorResponse.from(tutor)
    }

    @Transactional
    fun updateTutorById(tutorId: Long, request: UpdateTutorRequest): TutorResponse {
        val tutor = tutorRepository.findByIdOrNull(tutorId) ?: throw ModelNotFoundException("tutor", "id: $tutorId")

        tutor.apply {
            nickname = request.nickname
            description = request.description
            career = request.career
        }
        return tutorRepository.save(tutor).let { TutorResponse.from(tutor) }
    }

    @Transactional
    fun deleteTutorById(tutorId: Long) {
        val tutor = tutorRepository.findByIdOrNull(tutorId) ?: throw ModelNotFoundException("tutor", "id: $tutorId")
        tutorRepository.delete(tutor)
    }

    @Transactional
    fun followStudentAndTutor(tutorId: Long, studentId: Long): FollowResponse {
        val tutor = tutorRepository.findByIdOrNull(tutorId) ?: throw ModelNotFoundException("tutor", "id: $tutorId")
        val student =
            studentRepository.findByIdOrNull(studentId) ?: throw ModelNotFoundException("student", "id: $studentId")
        if (followRepository.existsById(
                FollowId(
                    studentId,
                    tutor.id!!
                )
            )
        ) throw IllegalStateException("already followed")
        return followRepository.save(Follow(FollowId(studentId, tutor.id!!), tutor, student))
            .let { FollowResponse.from(it) }

    }

    @Transactional
    fun unfollowStudentAndTutor(tutorId: Long, studentId: Long) {
        val tutor = tutorRepository.findByIdOrNull(tutorId) ?: throw ModelNotFoundException("tutor", "id: $tutorId")
        val follow = followRepository.findByIdOrNull(FollowId(studentId, tutor.id!!))
            ?: throw ModelNotFoundException("follow", "tutor id: $tutorId, studentId: $studentId")
        followRepository.delete(follow)
    }

    @Transactional
    fun registerIfAbsent(userInfo: OAuthLoginUserInfo): Student {
        return studentRepository.findByProviderNameAndProviderId(userInfo.provider.name, userInfo.id) ?: run {
            val student = Student(
                nickname = userInfo.nickname,
                email = userInfo.email,
                password = "",
                providerName = userInfo.provider.name,
                providerId = userInfo.id,
            )
            studentRepository.save(student)
        }
    }

}