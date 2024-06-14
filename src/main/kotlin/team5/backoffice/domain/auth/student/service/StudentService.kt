package team5.backoffice.domain.auth.student.service

import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import team5.backoffice.domain.auth.dto.ChangePasswordRequest
import team5.backoffice.domain.auth.dto.LoginRequest
import team5.backoffice.domain.auth.dto.SignUpRequest
import team5.backoffice.domain.user.dto.StudentResponse
import team5.backoffice.domain.user.model.Student
import team5.backoffice.domain.user.repository.StudentRepository
import team5.backoffice.infra.security.jwt.JwtPlugin
import javax.naming.AuthenticationException

@Service
class StudentService(
    private val studentRepository: StudentRepository,
    private val jwtPlugin: JwtPlugin,
    private val passwordEncoder: PasswordEncoder
) {
    fun signUpStudent(signUpRequest: SignUpRequest): StudentResponse {
        if (studentRepository.existsByEmail(signUpRequest.email)) {
            throw Exception("이미 존재하는 회원")
        }
        val student = Student(
            nickname = signUpRequest.nickname,
            email = signUpRequest.email,
            password = passwordEncoder.encode(signUpRequest.password),
            providerName = "",
            providerId = "",
//            prevPasswords = mutableListOf()
        )
        studentRepository.save(student)
        return StudentResponse.from(student)
    }

    fun loginStudent(loginRequest: LoginRequest): String {
        val student = studentRepository.findByEmail(loginRequest.email) ?: throw RuntimeException("student not found")

        return if (passwordEncoder.matches(loginRequest.password, student.password)) {
            jwtPlugin.generateAccessToken(
                subject = student.id.toString(),
                email = student.email,
                role = "STUDENT"
            )
        } else throw AuthenticationException("Password is incorrect")

    }

    fun changeStudentPassword(request: ChangePasswordRequest, studentEmail: String): Boolean {
        val student = studentRepository.findByEmail(studentEmail) ?: throw RuntimeException("student not found")
        if (passwordEncoder.matches(request.password, student.password)) {
            if (!passwordEncoder.matches(request.newPassword, student.password)) {
                student.password = passwordEncoder.encode(request.newPassword)
            } else throw AuthenticationException("Password not changed")
        } else throw AuthenticationException("Password is incorrect")
        return true
    }
}