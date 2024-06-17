package team5.backoffice.domain.auth.oauth.controller

import jakarta.servlet.http.HttpServletResponse
import org.springframework.web.bind.annotation.*
import team5.backoffice.domain.auth.oauth.OAuthClientService
import team5.backoffice.domain.auth.oauth.OAuthProviderConverter
import team5.backoffice.domain.auth.oauth.service.OAuthLoginService
import team5.backoffice.domain.auth.oauth.type.OAuthProvider

@RestController
@RequestMapping("/oauth")
class OAuthController(
    private val oAuthLoginService: OAuthLoginService,
    private val oAuthClientService: OAuthClientService,
    private val oAuthProviderConverter: OAuthProviderConverter,
) {
    @GetMapping("/{provider}")
    fun redirectLoginPage(
        @PathVariable provider: OAuthProvider,
        response: HttpServletResponse
    ) {
        oAuthClientService.getLoginPageUrl(provider)
            .let { response.sendRedirect(it) }
    }

    @GetMapping("/{provider}/callback")
    fun callback(
        @PathVariable provider: String,
        @RequestParam code: String
    ): String {
        val oAuthProvider = oAuthProviderConverter.convert(provider)
        return oAuthLoginService.login(oAuthProvider, code)
    }
}