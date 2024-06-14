package team5.backoffice.domain.exception


data class UnauthorizedUserException(
    override val message: String? = "Unauthorized user access"
) : RuntimeException()