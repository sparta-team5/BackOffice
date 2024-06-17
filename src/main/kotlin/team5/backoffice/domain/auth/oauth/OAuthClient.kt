package team5.backoffice.domain.auth.oauth

import team5.backoffice.domain.auth.oauth.type.OAuthProvider

interface OAuthClient {
    fun getLoginPageUrl(): String
    fun getAccessToken(authorizationCode: String): String
    fun retrieveUserInfo(accessToken: String): OAuthLoginUserInfo
    fun supports(provider: OAuthProvider): Boolean
}