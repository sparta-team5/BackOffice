package domain.course.model

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "category")
class Category(
    @Id
    @Column(name = "name")
    val name: String,

    @Column(name = "count")
    var count: Long
) {
}