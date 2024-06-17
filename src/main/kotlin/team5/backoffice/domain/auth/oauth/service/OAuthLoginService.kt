package team5.backoffice.domain.auth.oauth.service

import jakarta.transaction.Transactional
import org.springframework.stereotype.Service
import team5.backoffice.domain.auth.dto.TokenResponse
import team5.backoffice.domain.auth.oauth.OAuthClientService
import team5.backoffice.domain.auth.oauth.type.OAuthProvider
import team5.backoffice.domain.auth.student.service.StudentService
import team5.backoffice.domain.user.service.UserService

@Service
class OAuthLoginService(
    private val oAuthClientService: OAuthClientService,
    private val userService: UserService,
    private val studentService: StudentService,
) {
    @Transactional
    fun login(provider: OAuthProvider, code: String): TokenResponse {
        return oAuthClientService.login(provider, code)
            .let { userService.registerIfAbsent(it) }
            .let { studentService.generateTokensWithStudent(it) }
    }
}