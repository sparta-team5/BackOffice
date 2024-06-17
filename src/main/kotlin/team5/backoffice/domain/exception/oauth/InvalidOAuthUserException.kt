package team5.backoffice.domain.exception.oauth

data class InvalidOAuthUserException(val providerName: String) : RuntimeException(
    "Failed to retrieve userinfo from $providerName"
)