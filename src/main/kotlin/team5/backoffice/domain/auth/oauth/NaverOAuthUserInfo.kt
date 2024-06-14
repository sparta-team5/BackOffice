package team5.backoffice.domain.auth.oauth

data class NaverOAuthUserInfo(
    val resultcode: String,
    val message: String,
    val response: NaverUserInfoProperties
)

