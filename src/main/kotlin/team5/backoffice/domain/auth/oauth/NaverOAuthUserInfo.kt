package team5.backoffice.domain.auth.oauth

import com.fasterxml.jackson.annotation.JsonProperty

data class NaverOAuthUserInfo(
    @JsonProperty("resultcode")
    val resultCode: String,
    val message: String,
    val response: NaverUserInfoProperties
)

