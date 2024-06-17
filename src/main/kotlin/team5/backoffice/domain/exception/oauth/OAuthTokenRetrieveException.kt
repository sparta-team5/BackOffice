package team5.backoffice.domain.exception.oauth

data class OAuthTokenRetrieveException(val providerName: String) : RuntimeException(
    "Failed to retrieve access token from $providerName"
)