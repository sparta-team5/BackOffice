package domain.user.dto

import domain.user.model.Follow
import domain.user.model.Student

data class FollowResponseDto (
    val studentId : Long,
    val tutorId : Long
        ){

    companion object{
        fun from(follow : Follow):FollowResponseDto = FollowResponseDto(
            studentId = follow.student.id!!,
            tutorId = follow.student.id!!,
        )
    }
}