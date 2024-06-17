package team5.backoffice.domain.course.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import team5.backoffice.domain.course.model.View

@Repository
interface ViewRepository: JpaRepository<View, Long> {
}