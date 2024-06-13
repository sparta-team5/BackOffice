package team5.backoffice.domain.course.repository

import team5.backoffice.domain.course.model.Subscription
import team5.backoffice.domain.course.model.SubscriptionId
import org.springframework.data.jpa.repository.JpaRepository

interface SubscriptionRepository : JpaRepository<Subscription, SubscriptionId> {
    override fun existsById(subscriptionId: SubscriptionId): Boolean
}