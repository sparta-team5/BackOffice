package domain.user.repository

import domain.user.model.Tutor
import org.springframework.data.jpa.repository.JpaRepository

interface TutorRepository : JpaRepository<Tutor, Long> {
    fun existByEmail(email: String): Boolean
    fun findByEmail(email: String): Tutor
}