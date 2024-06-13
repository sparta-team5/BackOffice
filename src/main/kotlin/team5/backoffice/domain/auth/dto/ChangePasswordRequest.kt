package team5.backoffice.domain.auth.dto

data class ChangePasswordRequest(
    val password: String,
    val newPassword: String,
    val user: GetUserInfoRequest
)