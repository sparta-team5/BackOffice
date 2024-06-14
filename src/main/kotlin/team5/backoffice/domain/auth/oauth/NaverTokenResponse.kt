package team5.backoffice.domain.auth.oauth

import com.fasterxml.jackson.databind.PropertyNamingStrategy
import com.fasterxml.jackson.databind.annotation.JsonNaming

@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy::class)
data class NaverTokenResponse(
    val accessToken: String
)
