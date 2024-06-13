package domain.course.repository

import domain.course.model.Subscription
import domain.course.model.SubscriptionId
import org.springframework.data.jpa.repository.JpaRepository

interface SubscriptionRepository : JpaRepository<Subscription, SubscriptionId> {
    override fun existsById(subscriptionId: SubscriptionId): Boolean
}