package team5.backoffice.domain.user.repository

import org.springframework.data.jpa.repository.JpaRepository
import team5.backoffice.domain.user.model.Follow
import team5.backoffice.domain.user.model.FollowId

interface FollowRepository : JpaRepository<Follow, FollowId> {
    override fun existsById(followId: FollowId): Boolean
}