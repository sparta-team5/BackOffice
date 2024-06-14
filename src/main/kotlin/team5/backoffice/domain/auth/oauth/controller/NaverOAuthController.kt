package team5.backoffice.domain.auth.oauth.controller

import jakarta.servlet.http.HttpServletResponse
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import team5.backoffice.domain.auth.oauth.service.OAuthService

@RestController
class NaverOAuthController(
    private val oAuthService: OAuthService,
) {

    // 사용자가 로그인버튼 누름 -> 요청들어오는 엔드포인트
    @GetMapping("/oauth/naver")
    fun getNaverLoginPage(response: HttpServletResponse): String {
        return oAuthService.getLoginPage() //네이버로그인 페이지url가져오는 메서드 호출
    }

    @GetMapping("/naver/callback")
    fun naverCallback(
        @RequestParam code: String
    ): String {
        return oAuthService.naverLogin(code) //로그인 완료되면 코드라는 걸 돌려줌.
    }
}