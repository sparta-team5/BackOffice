package team5.backoffice.domain.course.repository

import team5.backoffice.domain.course.model.Category
import org.springframework.data.jpa.repository.JpaRepository

interface CategoryRepository : JpaRepository<Category, String> {
    fun findByName(name: String): Category?
}