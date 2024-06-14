package team5.backoffice.domain.auth.student.service

import org.springframework.stereotype.Service
import team5.backoffice.domain.auth.dto.ChangePasswordRequest
import team5.backoffice.domain.auth.dto.GetUserInfoRequest
import team5.backoffice.domain.auth.dto.LoginRequest
import team5.backoffice.domain.auth.dto.SignUpRequest
import team5.backoffice.domain.auth.jwt.BCHash
import team5.backoffice.domain.auth.jwt.JwtPlugin
import team5.backoffice.domain.user.dto.StudentResponse
import team5.backoffice.domain.user.model.Student
import team5.backoffice.domain.user.repository.StudentRepository
import javax.naming.AuthenticationException

@Service
class StudentService(
    private val studentRepository: StudentRepository,
    private val jwtPlugin: JwtPlugin,
    private val encoder: BCHash,
) {
    fun getStudentInfo(request: GetUserInfoRequest): Student {
        return validateStudentLoginEmailFromToken(request.token)
    }

    private fun validateStudentLoginEmailFromToken(token: String): Student {
        return jwtPlugin.validateToken(token).let {
            studentRepository.findByEmail(it)!!
        }
    }

    fun signUpStudent(signUpRequest: SignUpRequest): StudentResponse {
        if (studentRepository.existsByEmail(signUpRequest.email)) {
            throw Exception("이미 존재하는 회원")
        }
        val student = Student(
            nickname = signUpRequest.nickname,
            email = signUpRequest.email,
            password = encoder.hashPassword(signUpRequest.password),
            providerName = "",
            providerId = "",
//            prevPasswords = mutableListOf()
        )
        studentRepository.save(student)
        return StudentResponse.from(student)
    }

    fun loginStudent(loginRequest: LoginRequest): String {
        val student = studentRepository.findByEmail(loginRequest.email) ?: throw RuntimeException("student not found")
        if (!encoder.verifyPassword(
                loginRequest.password,
                student.password
            )
        ) throw AuthenticationException("Password is incorrect")
        return jwtPlugin.generateAccessToken("email", student.email)
    }

    fun changeStudentPassword(request: ChangePasswordRequest): Boolean {
        validateStudentLoginEmailFromToken(request.user.token)
            .let {
                if (encoder.verifyPassword(request.password, it.password)) {
                    if (!encoder.verifyPassword(request.newPassword, it.password)) {
                        it.password = encoder.hashPassword(request.newPassword)
                    } else throw AuthenticationException("Password not changed")
                } else throw AuthenticationException("Password is incorrect")
            }
        return true
    }
}