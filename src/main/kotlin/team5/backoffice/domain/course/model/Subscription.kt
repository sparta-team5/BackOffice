package team5.backoffice.domain.course.model

import jakarta.persistence.Column
import jakarta.persistence.EmbeddedId
import jakarta.persistence.Entity
import jakarta.persistence.Table
import java.time.LocalDateTime


@Entity
@Table(name = "subscription")
class Subscription(
    @EmbeddedId
    val id: SubscriptionId,

    @Column(name = "created_at")
    val createdAt: LocalDateTime,
) {
}