package team5.backoffice.domain.auth.oauth.google

import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpStatusCode
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.util.LinkedMultiValueMap
import org.springframework.web.client.RestClient
import org.springframework.web.client.body
import team5.backoffice.domain.auth.oauth.OAuthClient
import team5.backoffice.domain.auth.oauth.google.dto.GoogleOAuthUserInfo
import team5.backoffice.domain.auth.oauth.google.dto.GoogleTokenResponse
import team5.backoffice.domain.auth.oauth.type.OAuthProvider
import team5.backoffice.domain.exception.oauth.InvalidOAuthUserException
import team5.backoffice.domain.exception.oauth.OAuthTokenRetrieveException

@Component
class GoogleOAuthClient(
    @Value("\${oauth2.google.client_id}") val clientId: String,
    @Value("\${oauth2.google.client_secret}") val clientSecret: String,
    @Value("\${oauth2.google.redirect_url}") val redirectUrl: String,
    @Value("\${oauth2.google.auth_server_base_url}") val authServerBaseUrl: String,
    @Value("\${oauth2.google.auth_server_token_url}") val authServerTokenUrl: String,
    @Value("\${oauth2.google.resource_server_base_url}") val resourceServerBaseUrl: String,
    private val restClient: RestClient
) : OAuthClient {

    override fun getLoginPageUrl(): String {
        return StringBuilder(authServerBaseUrl)
            .append("?client_id=").append(clientId)
            .append("&redirect_uri=").append(redirectUrl)
            .append("&scope=email profile")
            .append("&response_type=code")
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
            .uri(authServerTokenUrl)
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .body(LinkedMultiValueMap<String, String>().apply { this.setAll(requestData) })
            .retrieve()
            .onStatus(HttpStatusCode::isError) { _, response ->
                throw OAuthTokenRetrieveException("GOOGLE: ${response.statusCode}")
            }
            .body<GoogleTokenResponse>()
            ?.accessToken
            ?: throw OAuthTokenRetrieveException("GOOGLE")

    }


    override fun retrieveUserInfo(accessToken: String): GoogleOAuthUserInfo {
        return restClient.get()
            .uri("$resourceServerBaseUrl/oauth2/v1/userinfo")
            .header("Authorization", "Bearer $accessToken")
            .retrieve()
            .onStatus(HttpStatusCode::isError) { _, response ->
                throw throw InvalidOAuthUserException("GOOGLE: ${response.statusCode}")
            }
            .body<GoogleOAuthUserInfo>()
            ?: throw throw InvalidOAuthUserException("GOOGLE")
    }

    override fun supports(provider: OAuthProvider): Boolean {
        return provider == OAuthProvider.GOOGLE
    }
}