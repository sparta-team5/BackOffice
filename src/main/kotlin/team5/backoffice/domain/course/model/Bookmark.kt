package team5.backoffice.domain.course.model

import jakarta.persistence.EmbeddedId
import jakarta.persistence.Entity
import jakarta.persistence.Table

@Entity
@Table(name = "bookmark")
class Bookmark(
    @EmbeddedId
    val id: BookmarkId
) {

}