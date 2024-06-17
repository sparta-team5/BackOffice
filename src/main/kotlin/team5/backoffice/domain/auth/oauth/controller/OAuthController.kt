package team5.backoffice.domain.auth.oauth.controller

import jakarta.servlet.http.HttpServletResponse
import org.springframework.web.bind.annotation.*
import team5.backoffice.domain.auth.dto.TokenResponse
import team5.backoffice.domain.auth.oauth.OAuthClientService
import team5.backoffice.domain.auth.oauth.service.OAuthLoginService
import team5.backoffice.domain.auth.oauth.type.OAuthProviderConverter

@RestController
@RequestMapping("/oauth")
class OAuthController(
    private val oAuthLoginService: OAuthLoginService,
    private val oAuthClientService: OAuthClientService,
    private val oAuthProviderConverter: OAuthProviderConverter,
) {
    @GetMapping("/{provider}")
    fun redirectLoginPage(
        @PathVariable provider: String,
        response: HttpServletResponse
    ) {
        val oAuthProvider = oAuthProviderConverter.convert(provider)
        oAuthClientService.getLoginPageUrl(oAuthProvider)
            .let { response.sendRedirect(it) }
    }

    @GetMapping("/{provider}/callback")
    fun callback(
        @PathVariable provider: String,
        @RequestParam code: String
    ): TokenResponse {
        val oAuthProvider = oAuthProviderConverter.convert(provider)
        return oAuthLoginService.login(oAuthProvider, code)
    }
}