package team5.backoffice.domain.auth.oauth.naver.dto

import com.fasterxml.jackson.annotation.JsonProperty
import team5.backoffice.domain.auth.oauth.OAuthLoginUserInfo
import team5.backoffice.domain.auth.oauth.type.OAuthProvider

data class NaverOAuthUserInfo(
    @JsonProperty("resultcode")
    val resultCode: String,
    val message: String,
    val response: NaverUserInfoProperties
) : OAuthLoginUserInfo(
    provider = OAuthProvider.NAVER,
    id = response.id,
    nickname = response.name,
    email = response.email
)

