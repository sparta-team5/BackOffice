package domain.auth.student.service

import domain.auth.dto.ChangePasswordRequest
import domain.auth.dto.GetUserInfoRequest
import domain.auth.dto.LoginRequest
import domain.auth.dto.SignUpRequest
import domain.auth.jwt.BCHash
import domain.auth.jwt.JwtPlugin
import domain.user.dto.StudentResponse
import domain.user.model.Student
import domain.user.repository.StudentRepository
import org.springframework.stereotype.Service
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
            studentRepository.findByEmail(it)
        }
    }

    fun signUpStudent(signUpRequest: SignUpRequest): StudentResponse {
        if (studentRepository.existByEmail(signUpRequest.email)) {
            throw Exception("이미 존재하는 회원")
        }
        val student = Student(
            nickname = signUpRequest.nickname,
            email = signUpRequest.email,
            password = encoder.hashPassword(signUpRequest.password),
            providerName = null.toString(),
            providerId = null.toString(),
            prevPasswords = null.toString()
        )
        studentRepository.save(student)
        return StudentResponse.from(student)
    }

    fun loginStudent(loginRequest: LoginRequest): String {
        val token = studentRepository.findByEmail(loginRequest.email)
            .let { student ->
                if (encoder.verifyPassword(loginRequest.password, student.password)) {
                    jwtPlugin.generateAccessToken("email", student.email)
                } else throw AuthenticationException("Password is incorrect")
            }
        return token
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