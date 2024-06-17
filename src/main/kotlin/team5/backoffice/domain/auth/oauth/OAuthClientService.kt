package team5.backoffice.domain.auth.oauth

import jakarta.transaction.NotSupportedException
import org.springframework.stereotype.Service
import team5.backoffice.domain.auth.oauth.type.OAuthProvider

@Service
class OAuthClientService(
    private val clients: List<OAuthClient>
) {
    fun getLoginPageUrl(provider: OAuthProvider): String {
        return this.selectClient(provider).getLoginPageUrl()
    }

    fun login(provider: OAuthProvider, code: String): OAuthLoginUserInfo {
        val client = this.selectClient(provider)
        return client.getAccessToken(code)
            .let { client.retrieveUserInfo(it) }
    }

    private fun selectClient(provider: OAuthProvider): OAuthClient {
        return clients.find { it.supports(provider) }
            ?: throw NotSupportedException("OAuth provider not found")
    }
}