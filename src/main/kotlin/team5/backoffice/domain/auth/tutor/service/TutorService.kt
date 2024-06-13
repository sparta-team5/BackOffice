package team5.backoffice.domain.auth.tutor.service

import org.springframework.stereotype.Service
import team5.backoffice.domain.auth.dto.ChangePasswordRequest
import team5.backoffice.domain.auth.dto.GetUserInfoRequest
import team5.backoffice.domain.auth.dto.LoginRequest
import team5.backoffice.domain.auth.dto.SignUpRequest
import team5.backoffice.domain.auth.jwt.BCHash
import team5.backoffice.domain.auth.jwt.JwtPlugin
import team5.backoffice.domain.user.dto.TutorResponse
import team5.backoffice.domain.user.model.Tutor
import team5.backoffice.domain.user.repository.TutorRepository
import javax.naming.AuthenticationException

@Service
class TutorService(
    private val tutorRepository: TutorRepository,
    private val jwtPlugin: JwtPlugin,
    private val encoder: BCHash,
) {
    fun getTutorInfo(request: GetUserInfoRequest): Tutor {
        return validateTutorLoginEmailFromToken(request.token)
    }

    private fun validateTutorLoginEmailFromToken(token: String): Tutor {
        return jwtPlugin.validateToken(token).let {
            tutorRepository.findByEmail(it)
        }
    }

    fun signUpTutor(signUpRequest: SignUpRequest): TutorResponse {
        if (tutorRepository.existsByEmail(signUpRequest.email)) {
            throw Exception("이미 존재하는 회원")
        }
        val tutor = Tutor(
            nickname = signUpRequest.nickname,
            email = signUpRequest.email,
            password = encoder.hashPassword(signUpRequest.password),
            description = null.toString(),
            career = null.toString(),
            prevPasswords = null.toString()
        )
        tutorRepository.save(tutor)
        return TutorResponse.from(tutor)
    }

    fun loginTutor(loginRequest: LoginRequest): String {
        val token = tutorRepository.findByEmail(loginRequest.email)
            .let { tutor ->
                if (encoder.verifyPassword(loginRequest.password, tutor.password)) {
                    jwtPlugin.generateAccessToken("email", tutor.email)
                } else throw AuthenticationException("Password is incorrect")
            }
        return token
    }

    //단순 변경(이전 비밀번호 조회 X)
    fun changeTutorPassword(request: ChangePasswordRequest): Boolean {
        validateTutorLoginEmailFromToken(request.user.token)
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