package team5.backoffice.domain.course.repository

import org.springframework.data.jpa.repository.JpaRepository
import team5.backoffice.domain.course.model.Category

interface CategoryRepository : JpaRepository<Category, String> {
    fun findByName(name: String): Category?
}