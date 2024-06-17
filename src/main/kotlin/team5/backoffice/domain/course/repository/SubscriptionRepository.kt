package team5.backoffice.domain.course.repository

import org.springframework.data.jpa.repository.JpaRepository
import team5.backoffice.domain.course.model.Subscription
import team5.backoffice.domain.course.model.SubscriptionId

interface SubscriptionRepository : JpaRepository<Subscription, SubscriptionId> {
    override fun existsById(subscriptionId: SubscriptionId): Boolean
}