package team5.backoffice.domain.auth.student.service

import jakarta.transaction.Transactional
import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import team5.backoffice.domain.auth.dto.ChangePasswordRequest
import team5.backoffice.domain.auth.dto.LoginRequest
import team5.backoffice.domain.auth.dto.SignUpRequest
import team5.backoffice.domain.exception.ModelNotFoundException
import team5.backoffice.domain.exception.PasswordIncorrectException
import team5.backoffice.domain.exception.RecentlyUsedPasswordException
import team5.backoffice.domain.user.dto.StudentResponse
import team5.backoffice.domain.user.model.Student
import team5.backoffice.domain.user.repository.StudentRepository
import team5.backoffice.infra.security.jwt.JwtPlugin

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
        )
        studentRepository.save(student)
        return StudentResponse.from(student)
    }

    fun loginStudent(loginRequest: LoginRequest): String {
        val student = studentRepository.findByEmail(loginRequest.email) ?: throw ModelNotFoundException(
            "student",
            "email: ${loginRequest.email}"
        )
        if (!passwordEncoder.matches(
                loginRequest.password,
                student.password
            )
        ) throw PasswordIncorrectException()
        return jwtPlugin.generateAccessToken(subject = student.id.toString(), email = student.email, role = "STUDENT")
    }

    @Transactional
    fun changeStudentPassword(request: ChangePasswordRequest, studentId: Long): String {
        val student =
            studentRepository.findByIdOrNull(studentId) ?: throw ModelNotFoundException("student", "id: $studentId")
        if (student.providerId != null) throw IllegalStateException("social login user")

        val newPasswordEncoded = passwordEncoder.encode(request.newPassword)
        if (!passwordEncoder.matches(
                request.password,
                student.password
            )
        ) throw PasswordIncorrectException()

        if (!isNewPasswordValid(student, request.newPassword)) throw RecentlyUsedPasswordException()
        student.changePassword(newPasswordEncoded)
        return "password changed"
    }

    fun isNewPasswordValid(student: Student, newPassword: String): Boolean {
        if (passwordEncoder.matches(newPassword, student.password)) return false
        student.oldPassword1?.let { if (passwordEncoder.matches(newPassword, student.oldPassword1)) return false }
        student.oldPassword2?.let { if (passwordEncoder.matches(newPassword, student.oldPassword2)) return false }
        return true
    }
}