package team5.backoffice.domain.course.repository

import org.springframework.data.jpa.repository.JpaRepository
import team5.backoffice.domain.course.model.Bookmark
import team5.backoffice.domain.course.model.BookmarkId

interface BookmarkRepository : JpaRepository<Bookmark, BookmarkId> {
    override fun existsById(bookmarkId: BookmarkId): Boolean
}