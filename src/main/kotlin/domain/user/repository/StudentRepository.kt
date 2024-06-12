package domain.user.repository

import domain.user.model.Student
import org.springframework.data.jpa.repository.JpaRepository

interface StudentRepository : JpaRepository<Student, Long> {

    fun existByEmail(email:String):Boolean

}