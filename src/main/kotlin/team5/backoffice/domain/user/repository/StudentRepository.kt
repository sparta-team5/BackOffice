package team5.backoffice.domain.user.repository

import org.springframework.data.jpa.repository.JpaRepository
import team5.backoffice.domain.user.model.Student

interface StudentRepository : JpaRepository<Student, Long> {
    fun existsByEmail(email: String): Boolean
    fun findByEmail(email: String): Student
}