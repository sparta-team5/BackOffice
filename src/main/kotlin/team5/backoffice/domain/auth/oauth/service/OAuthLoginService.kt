package team5.backoffice.domain.auth.oauth.service

import org.springframework.stereotype.Service
import team5.backoffice.domain.auth.oauth.OAuthClientService
import team5.backoffice.domain.auth.oauth.type.OAuthProvider
import team5.backoffice.domain.user.service.UserService
import team5.backoffice.infra.security.jwt.JwtPlugin

@Service
class OAuthLoginService(
    private val oAuthClientService: OAuthClientService,
    private val userService: UserService,
    private val jwtPlugin: JwtPlugin,
) {

    fun login(provider: OAuthProvider, code: String): String {
        return oAuthClientService.login(provider, code)
            .let { userService.registerIfAbsent(it) }
            .let { jwtPlugin.generateAccessToken(it.id!!.toString(), it.email, "STUDENT") }
    }
}