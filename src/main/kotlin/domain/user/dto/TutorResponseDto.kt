package domain.user.dto

import domain.user.model.Tutor

data class TutorResponseDto (
        val id:Long,
        val email:String
        ){

        companion object{
                fun from(tutor : Tutor):TutorResponseDto = TutorResponseDto(
                        id = tutor.id!!,
                        email = tutor.email
                )
        }
}