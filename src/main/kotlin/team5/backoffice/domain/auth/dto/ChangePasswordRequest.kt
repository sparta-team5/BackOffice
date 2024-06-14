package team5.backoffice.domain.auth.dto

import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.Size

data class ChangePasswordRequest(
    val password: String,
    @field: NotEmpty
    @field:Size(message = "password length should be 8 ~15", min = 8, max = 15)
    val newPassword: String,
)