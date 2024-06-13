package domain.course.repository

import domain.course.model.Bookmark
import domain.course.model.BookmarkId
import org.springframework.data.jpa.repository.JpaRepository

interface BookmarkRepository : JpaRepository<Bookmark, BookmarkId> {
    override fun existsById(bookmarkId: BookmarkId): Boolean
}