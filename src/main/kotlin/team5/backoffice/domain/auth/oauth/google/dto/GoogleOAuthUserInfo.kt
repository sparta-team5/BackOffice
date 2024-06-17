package team5.backoffice.domain.auth.oauth.google.dto

import com.fasterxml.jackson.annotation.JsonProperty
import team5.backoffice.domain.auth.oauth.OAuthLoginUserInfo
import team5.backoffice.domain.auth.oauth.type.OAuthProvider

data class GoogleOAuthUserInfo(
    @JsonProperty("id") val providerId: String,
    @JsonProperty("name") val name: String,
    @JsonProperty("email") val googleEmail: String,
) : OAuthLoginUserInfo(
    provider = OAuthProvider.GOOGLE,
    id = providerId,
    nickname = name,
    email = googleEmail,
)

