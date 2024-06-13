package domain.user.dto

import domain.user.model.Follow

data class FollowResponse(
    val studentId: Long,
    val tutorId: Long
) {

    companion object {
        fun from(follow: Follow): FollowResponse = FollowResponse(
            studentId = follow.id.studentId,
            tutorId = follow.id.tutorId,
        )
    }
}