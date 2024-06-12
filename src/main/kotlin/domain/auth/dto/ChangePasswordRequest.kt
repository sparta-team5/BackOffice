package domain.auth.dto

data class ChangePasswordRequest(
    val password: String,
    val newPassword: String,
    val user: GetUserInfoRequest
)