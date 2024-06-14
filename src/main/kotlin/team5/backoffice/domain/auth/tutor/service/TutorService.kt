package team5.backoffice.domain.auth.tutor.service

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
import team5.backoffice.domain.user.dto.TutorResponse
import team5.backoffice.domain.user.model.Tutor
import team5.backoffice.domain.user.repository.TutorRepository
import team5.backoffice.infra.security.jwt.JwtPlugin

@Service
class TutorService(
    private val tutorRepository: TutorRepository,
    private val jwtPlugin: JwtPlugin,
    private val passwordEncoder: PasswordEncoder,
) {
    fun signUpTutor(signUpRequest: SignUpRequest): TutorResponse {
        if (tutorRepository.existsByEmail(signUpRequest.email)) throw Exception("이미 존재하는 회원")

        val tutor = Tutor(
            nickname = signUpRequest.nickname,
            email = signUpRequest.email,
            password = passwordEncoder.encode(signUpRequest.password),
            description = "",
            career = "",
        )
        tutorRepository.save(tutor)
        return TutorResponse.from(tutor)
    }

    fun loginTutor(loginRequest: LoginRequest): String {
        val tutor = tutorRepository.findByEmail(loginRequest.email) ?: throw ModelNotFoundException(
            "tutor",
            "email: ${loginRequest.email}"
        )
        if (!passwordEncoder.matches(
                loginRequest.password,
                tutor.password
            )
        ) throw PasswordIncorrectException()
        return jwtPlugin.generateAccessToken(subject = tutor.id.toString(), email = tutor.email, role = "TUTOR")

    }

    @Transactional
    fun changeTutorPassword(request: ChangePasswordRequest, tutorId: Long): String {
        val tutor = tutorRepository.findByIdOrNull(tutorId) ?: throw ModelNotFoundException("tutor", "id: $tutorId")
        if (!passwordEncoder.matches(
                request.password,
                tutor.password
            )
        ) throw PasswordIncorrectException()

        if (!isNewPasswordValid(
                tutor,
                request.newPassword
            )
        ) throw RecentlyUsedPasswordException()
        tutor.changePassword(passwordEncoder.encode(request.newPassword))
        return "password changed"
    }

    fun isNewPasswordValid(tutor: Tutor, newPassword: String): Boolean {
        if (passwordEncoder.matches(newPassword, tutor.password)) return false
        tutor.oldPassword1?.let { if (passwordEncoder.matches(newPassword, tutor.oldPassword1)) return false }
        tutor.oldPassword2?.let { if (passwordEncoder.matches(newPassword, tutor.oldPassword2)) return false }
        return true
    }
}