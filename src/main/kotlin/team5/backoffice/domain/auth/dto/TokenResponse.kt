package team5.backoffice.domain.auth.dto

data class TokenResponse(
    val accessToken: String,
    val refreshToken: String
)