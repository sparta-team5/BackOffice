package team5.backoffice.domain.auth.student.service

import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import team5.backoffice.domain.auth.dto.ChangePasswordRequest
import team5.backoffice.domain.auth.dto.GetUserInfoRequest
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
    fun getStudentInfo(request: GetUserInfoRequest): Student {
        return validateStudentLoginEmailFromToken(request.token)
    }

    private fun validateStudentLoginEmailFromToken(token: String): Student {
        return jwtPlugin.validateToken(token).getOrNull()?.let {
            val email = it.body["email"] as String
            studentRepository.findByEmail(email)
        } ?: throw AuthenticationException("Invalid token")
    }

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
        val token = studentRepository.findByEmail(loginRequest.email)
            .let { student ->
                if (passwordEncoder.matches(loginRequest.password, student.password)) {
                    jwtPlugin.generateAccessToken("email", student.email, "student")
                } else throw AuthenticationException("Password is incorrect")
            }
        return token
    }

    @PreAuthorize("hasRole('ROLE_STUDENT')")
    fun changeStudentPassword(request: ChangePasswordRequest): Boolean {
        validateStudentLoginEmailFromToken(request.user.token)
            .let {
                if (passwordEncoder.matches(request.password, it.password)) {
                    if (!passwordEncoder.matches(request.newPassword, it.password)) {
                        it.password = passwordEncoder.encode(request.newPassword)
                    } else throw AuthenticationException("Password not changed")
                } else throw AuthenticationException("Password is incorrect")
            }
        return true
    }
}