package team5.backoffice.domain.user.dto

import team5.backoffice.domain.user.model.Tutor

data class TutorResponse(
    val id: Long,
    val email: String,
    val nickname: String,
    val career: String,
    val description: String,
) {

    companion object {
        fun from(tutor: Tutor): TutorResponse = TutorResponse(
            id = tutor.id!!,
            email = tutor.email,
            nickname = tutor.nickname,
            career = tutor.career,
            description = tutor.description
        )
    }
}