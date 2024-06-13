package domain.course.model

import jakarta.persistence.EmbeddedId
import jakarta.persistence.Entity
import jakarta.persistence.Table


@Entity
@Table(name = "subscription")
class Subscription(
    @EmbeddedId
    val id: SubscriptionId
) {
}