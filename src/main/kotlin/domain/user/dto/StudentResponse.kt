package domain.user.dto

import domain.user.model.Student

data class StudentResponse(
    val id: Long,
    val email: String
) {

    companion object {
        fun from(student: Student): StudentResponse = StudentResponse(
            id = student.id!!,
            email = student.email
        )
    }
}