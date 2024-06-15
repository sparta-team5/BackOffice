package team5.backoffice.domain.exception

data class PasswordIncorrectException(
    override val message: String? = "Password Incorrect"
) : RuntimeException()