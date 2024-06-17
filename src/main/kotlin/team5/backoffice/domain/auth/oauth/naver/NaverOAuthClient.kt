package team5.backoffice.domain.auth.oauth.naver

import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpStatusCode
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.util.LinkedMultiValueMap
import org.springframework.web.client.RestClient
import org.springframework.web.client.body
import team5.backoffice.domain.auth.oauth.OAuthClient
import team5.backoffice.domain.auth.oauth.naver.dto.NaverOAuthUserInfo
import team5.backoffice.domain.auth.oauth.naver.dto.NaverTokenResponse
import team5.backoffice.domain.auth.oauth.type.OAuthProvider
import team5.backoffice.domain.exception.oauth.InvalidOAuthUserException
import team5.backoffice.domain.exception.oauth.OAuthTokenRetrieveException

@Component
class NaverOAuthClient(
    @Value("\${oauth2.naver.client_id}") val clientId: String,
    @Value("\${oauth2.naver.client_secret}") val clientSecret: String,
    @Value("\${oauth2.naver.redirect_url}") val redirectUrl: String,
    @Value("\${oauth2.naver.auth_server_base_url}") val authServerBaseUrl: String,
    @Value("\${oauth2.naver.resource_server_base_url}") val resourceServerBaseUrl: String,
    private val restClient: RestClient
) : OAuthClient {

    override fun getLoginPageUrl(): String {
        return StringBuilder(authServerBaseUrl)
            .append("/oauth2.0/authorize")
            .append("?client_id=").append(clientId)
            .append("&redirect_uri=").append(redirectUrl)
            .append("&response_type=").append("code")
            .toString()
    }

    override fun getAccessToken(authorizationCode: String): String {
        val requestData = mutableMapOf(
            "grant_type" to "authorization_code",
            "client_id" to clientId,
            "client_secret" to clientSecret,
            "redirect_uri" to redirectUrl,
            "code" to authorizationCode,
        )

        return restClient.post()
            .uri("$authServerBaseUrl/oauth2.0/token")
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .body(LinkedMultiValueMap<String, String>().apply { this.setAll(requestData) })
            .retrieve()
            .onStatus(HttpStatusCode::isError) { _, response ->
                throw OAuthTokenRetrieveException("NAVER: ${response.statusCode}")
            }
            .body<NaverTokenResponse>()
            ?.accessToken
            ?: throw OAuthTokenRetrieveException("NAVER")

    }


    override fun retrieveUserInfo(accessToken: String): NaverOAuthUserInfo {
        return restClient.get()
            .uri("$resourceServerBaseUrl/v1/nid/me")
            .header("Authorization", "Bearer $accessToken")
            .retrieve()
            .onStatus(HttpStatusCode::isError) { _, response ->
                throw InvalidOAuthUserException("NAVER: ${response.statusCode}")
            }
            .body<NaverOAuthUserInfo>()
            ?: throw InvalidOAuthUserException("NAVER")
    }

    override fun supports(provider: OAuthProvider): Boolean {
        return provider == OAuthProvider.NAVER
    }
}