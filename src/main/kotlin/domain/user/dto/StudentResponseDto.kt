package domain.user.dto

import domain.user.model.Student
import domain.user.model.Tutor

data class StudentResponseDto(
    val id:Long,
    val email:String
){

    companion object{
        fun from(student : Student):StudentResponseDto = StudentResponseDto(
            id = student.id!!,
            email = student.email
        )
    }
}