package domain.auth.dto

data class SignUpRequest(
    val nickname: String,
    val email: String,
    val password: String,
)