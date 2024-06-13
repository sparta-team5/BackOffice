package domain.user.dto

data class UpdateTutorRequest(
    val nickname: String,
    val description: String,
    val career: String
)