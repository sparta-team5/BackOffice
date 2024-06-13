package team5.backoffice.domain.user.repository

import org.springframework.data.jpa.repository.JpaRepository
import team5.backoffice.domain.user.model.Tutor

interface TutorRepository : JpaRepository<Tutor, Long> {
    fun existsByEmail(email: String): Boolean
    fun findByEmail(email: String): Tutor
}