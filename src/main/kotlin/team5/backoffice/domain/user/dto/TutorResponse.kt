package team5.backoffice.domain.user.dto

import team5.backoffice.domain.user.model.Tutor

data class TutorResponse(
    val id: Long,
    val email: String
) {

    companion object {
        fun from(tutor: Tutor): TutorResponse = TutorResponse(
            id = tutor.id!!,
            email = tutor.email
        )
    }
}