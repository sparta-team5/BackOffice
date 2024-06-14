package team5.backoffice.domain.auth.tutor.service

import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import team5.backoffice.domain.auth.dto.ChangePasswordRequest
import team5.backoffice.domain.auth.dto.LoginRequest
import team5.backoffice.domain.auth.dto.SignUpRequest
import team5.backoffice.domain.user.dto.TutorResponse
import team5.backoffice.domain.user.model.Tutor
import team5.backoffice.domain.user.repository.TutorRepository
import team5.backoffice.infra.security.jwt.JwtPlugin
import javax.naming.AuthenticationException

@Service
class TutorService(
    private val tutorRepository: TutorRepository,
    private val jwtPlugin: JwtPlugin,
    private val passwordEncoder: PasswordEncoder,
) {
    fun signUpTutor(signUpRequest: SignUpRequest): TutorResponse {
        if (tutorRepository.existsByEmail(signUpRequest.email)) {
            throw Exception("이미 존재하는 회원")
        }
        val tutor = Tutor(
            nickname = signUpRequest.nickname,
            email = signUpRequest.email,
            password = passwordEncoder.encode(signUpRequest.password),
            description = "",
            career = "",
//            prevPasswords = mutableListOf()
        )
        tutorRepository.save(tutor)
        return TutorResponse.from(tutor)
    }

    fun loginTutor(loginRequest: LoginRequest): String {
        val token = tutorRepository.findByEmail(loginRequest.email)
            .let { tutor ->
                if (passwordEncoder.matches(loginRequest.password, tutor.password)) {
                    jwtPlugin.generateAccessToken("email", tutor.email, "TUTOR")
                } else throw AuthenticationException("Password is incorrect")
            }
        return token
    }

    fun changeTutorPassword(request: ChangePasswordRequest, tutorEmail: String): Boolean {
        val tutor = tutorRepository.findByEmail(tutorEmail)
        if (passwordEncoder.matches(request.password, tutor.password)) {
            if (passwordEncoder.matches(request.newPassword, tutor.password)) {
                tutor.password = passwordEncoder.encode(request.newPassword)
            } else throw AuthenticationException("Password not changed")
        } else throw AuthenticationException("Password is incorrect")
        return true
    }
}