package team5.backoffice.domain.auth.oauth

import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpStatusCode
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.util.LinkedMultiValueMap
import org.springframework.web.client.RestClient
import org.springframework.web.client.body

@Component
class NaverOAuthClient(
    @Value("\${oauth2.naver.client_id}") val clientId: String,
    @Value("\${oauth2.naver.client_secret}") val clientSecret: String,
    @Value("\${oauth2.naver.redirect_url}") val redirectUrl: String,
    @Value("\${oauth2.naver.auth_server_base_url}") val authServerBaseUrl: String,
    @Value("\${oauth2.naver.resource_server_base_url}") val resourceServerBaseUrl: String,
    private val restClient: RestClient
) {

    //네이버 로그인 페이지 url을 구성
    fun getLoginPageUrl(): String {
        return StringBuilder(authServerBaseUrl)
            .append("/oauth2.0/authorize")
            .append("?client_id=").append(clientId)
            .append("&redirect_uri=").append(redirectUrl)
            .append("&response_type=").append("code")
            .toString()
    }

    //로그인 후 인가코드로 accesstoken받아옴
    fun getAccessToken(code: String): String {
        val requestData = mutableMapOf(
            "grant_type" to "authorization_code",
            "client_id" to clientId,
            "client_secret" to clientSecret,
            "redirect_uri" to redirectUrl,
            "code" to code,
        )

        return restClient.post()
            .uri("$authServerBaseUrl/oauth2.0/token")
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .body(LinkedMultiValueMap<String, String>().apply { this.setAll(requestData) })
            .retrieve()
            .onStatus(HttpStatusCode::isError) { _, response ->
                throw RuntimeException("${response.statusCode} naver AccessToken 조회 실패")
            }
            .body<NaverTokenResponse>()
            ?.accessToken
            ?: throw RuntimeException("naver AccessToken 조회 실패")

    }

    //access토큰 바탕으로 사용자정보요청
    fun getUserInfo(accessToken: String): NaverOAuthUserInfo {
        return restClient.get()
            .uri("$resourceServerBaseUrl/v1/nid/me")
            .header("Authorization", "Bearer $accessToken")
            .retrieve()
            .onStatus(HttpStatusCode::isError) { _, response ->
                throw RuntimeException("${response.statusCode} naver user 조회 실패")
            }
            .body<NaverOAuthUserInfo>()
            ?: throw RuntimeException("naver user조회 실패")
    }
}