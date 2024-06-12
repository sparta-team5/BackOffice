package domain.user.dto

import domain.user.model.Follow

data class FollowResponseDto(
    val studentId: Long,
    val tutorId: Long
) {

    companion object {
        fun from(follow: Follow): FollowResponseDto = FollowResponseDto(
            studentId = follow.id.studentId,
            tutorId = follow.id.tutorId,
        )
    }
}