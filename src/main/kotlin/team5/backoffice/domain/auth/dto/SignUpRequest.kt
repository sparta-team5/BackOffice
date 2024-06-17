package team5.backoffice.domain.auth.dto

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.Pattern

data class SignUpRequest(
    @field:NotEmpty
    val nickname: String,

    @field:Email(message = "Not a valid email")
    val email: String,

    @field:Pattern(
        regexp = "^[a-zA-Z0-9!@#$%^&*]{8,15}$",
        message = "Passwords should be in 8~15 length with a-z, A-Z, 0~9, !@#$%^&*"
    )
    val password: String,
)