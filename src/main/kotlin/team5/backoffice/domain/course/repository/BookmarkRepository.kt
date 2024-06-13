package team5.backoffice.domain.course.repository

import team5.backoffice.domain.course.model.Bookmark
import team5.backoffice.domain.course.model.BookmarkId
import org.springframework.data.jpa.repository.JpaRepository

interface BookmarkRepository : JpaRepository<Bookmark, BookmarkId> {
    override fun existsById(bookmarkId: BookmarkId): Boolean
}