package team5.backoffice.domain.user.service

import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import team5.backoffice.domain.user.dto.*
import team5.backoffice.domain.user.repository.StudentRepository
import team5.backoffice.domain.user.repository.TutorRepository

@Service
class UserService(
    private val studentRepository: StudentRepository,
    private val tutorRepository: TutorRepository,
) {
    fun getStudentById(studentId: Long): StudentResponse {
        val student = studentRepository.findByIdOrNull(studentId) ?: throw RuntimeException("Student")
        return StudentResponse.from(student)
    }

    @PreAuthorize("hasRole('ROLE_STUDENT')")
    @Transactional
    fun updateStudentById(studentId: Long, request: UpdateStudentRequest): StudentResponse {
        val student = studentRepository.findByIdOrNull(studentId) ?: throw RuntimeException("Student")
        //todo : null이면 예외 던지기
        //todo : user가져오기
        //todo : 본인이 아니면 throw IllegalAccessException
        student.apply {
            this.nickname = request.nickname
        }
        return studentRepository.save(student).let { StudentResponse.from(student) }
    }

    @PreAuthorize("hasRole('ROLE_STUDENT')")
    fun deleteStudentById(studentId: Long) {
        val student = studentRepository.findByIdOrNull(studentId) ?: throw RuntimeException("Student")
        //todo : null이면 예외 던지기
        //todo : 토큰에서 user가져오기
        //todo : 본인이 아니면 throw IllegalAccessException
        studentRepository.delete(student)
    }

    //todo : admin auth ?
//    fun createTutor(){
//
//    }

    fun getTutorById(tutorId: Long): TutorResponse {
        val tutor = tutorRepository.findByIdOrNull(tutorId) ?: throw RuntimeException("Tutor")
        return TutorResponse.from(tutor)
    }

    @PreAuthorize("hasRole('ROLE_TUTOR')")
    fun updateTutorById(tutorId: Long, request: UpdateTutorRequest): TutorResponse {
        val tutor = tutorRepository.findByIdOrNull(tutorId) ?: throw RuntimeException("Tutor")
        //todo : null이면 예외 던지기
        //todo : 토큰에서 user가져오기
        //todo : 본인이 아니면 throw IllegalAccessException
        tutor.apply {
            nickname = request.nickname
            description = request.description
            career = request.career
        }
        return tutorRepository.save(tutor).let { TutorResponse.from(tutor) }
    }

    @PreAuthorize("hasRole('ROLE_TUTOR')")
    fun deleteTutorById(tutorId: Long) {
        val tutor = tutorRepository.findByIdOrNull(tutorId) ?: throw RuntimeException("Tutor")
        //todo : null이면 예외 던지기

        //todo : 토큰에서 user가져오기
        //todo : 본인이 아니면 throw IllegalAccessException
        tutorRepository.delete(tutor)
    }

    //todo
    fun followStudentAndUser(tutorId: Long): FollowResponse {
        val tutor = tutorRepository.findByIdOrNull(tutorId) ?: throw RuntimeException("Tutor")
        //todo : follow 요청한 Userid 토큰에서 가져오기
        TODO()

    }

    fun unfollowStudentAndUser(tutorId: Long) {
        val tutor = tutorRepository.findByIdOrNull(tutorId) ?: throw RuntimeException("Tutor")
        //todo : unfollow 요청한 Userid 토큰에서 가져오기

    }

}