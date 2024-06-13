package domain.user.service

import domain.user.dto.*
import domain.user.repository.StudentRepository
import domain.user.repository.TutorRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class UserService(
    private val studentRepository: StudentRepository,
    private val tutorRepository: TutorRepository,
) {


    fun getStudentById(studentId: Long): StudentResponseDto {
        val student = studentRepository.findByIdOrNull(studentId) ?: throw RuntimeException("Student")

        return StudentResponseDto.from(student)
    }


    @Transactional
    fun updateStudentById(studentId: Long, request: UpdateStudentRequestDto): StudentResponseDto {

        val student = studentRepository.findByIdOrNull(studentId) ?: throw RuntimeException("Student")
        //todo : null이면 예외 던지기

        //todo : user가져오기
        //todo : 본인이 아니면 throw IllegalAccessException


        val nickname = request.nickname
        return StudentResponseDto.from(student)

    }

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

    fun getTutorById(tutorId: Long): TutorResponseDto {

        val tutor = tutorRepository.findByIdOrNull(tutorId) ?: throw RuntimeException("Tutor")

        return TutorResponseDto.from(tutor)

    }

    fun updateTutorById(tutorId: Long, request: UpdateTutorRequestDto): TutorResponseDto {

        val tutor = tutorRepository.findByIdOrNull(tutorId) ?: throw RuntimeException("Tutor")

        //todo : null이면 예외 던지기

        //todo : 토큰에서 user가져오기
        //todo : 본인이 아니면 throw IllegalAccessException

        tutor.apply {
            nickname = request.nickname
            description = request.description
            career = request.career

        }

        return TutorResponseDto.from(tutor)

    }

    fun deleteTutorById(tutorId: Long) {

        val tutor = tutorRepository.findByIdOrNull(tutorId) ?: throw RuntimeException("Tutor")
        //todo : null이면 예외 던지기

        //todo : 토큰에서 user가져오기
        //todo : 본인이 아니면 throw IllegalAccessException

        tutorRepository.delete(tutor)
    }

    //todo
    fun followStudentAndUser(tutorId: Long): FollowResponseDto {
        val tutor = tutorRepository.findByIdOrNull(tutorId) ?: throw RuntimeException("Tutor")

        //todo : follow 요청한 Userid 토큰에서 가져오기
        TODO()

    }

    fun unfollowStudentAndUser(tutorId: Long) {
        val tutor = tutorRepository.findByIdOrNull(tutorId) ?: throw RuntimeException("Tutor")

        //todo : unfollow 요청한 Userid 토큰에서 가져오기

    }

}