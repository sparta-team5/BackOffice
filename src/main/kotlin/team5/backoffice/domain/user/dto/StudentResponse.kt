package team5.backoffice.domain.user.dto

import team5.backoffice.domain.user.model.Student

data class StudentResponse(
    val id: Long,
    val email: String,
    val nickname: String,
) {

    companion object {
        fun from(student: Student): StudentResponse = StudentResponse(
            id = student.id!!,
            email = student.email,
            nickname = student.nickname,
        )
    }
}