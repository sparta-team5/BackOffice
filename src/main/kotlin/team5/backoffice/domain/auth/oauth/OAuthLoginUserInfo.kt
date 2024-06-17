package team5.backoffice.domain.auth.oauth

import team5.backoffice.domain.auth.oauth.type.OAuthProvider

open class OAuthLoginUserInfo(
    val provider: OAuthProvider,
    val id: String,
    val nickname: String,
    val email: String,
)