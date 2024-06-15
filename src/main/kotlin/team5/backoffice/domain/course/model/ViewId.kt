package team5.backoffice.domain.course.model

import jakarta.persistence.Column
import jakarta.persistence.Embeddable
import java.io.Serializable

@Embeddable
data class ViewId(

    @Column(name = "course_id")
    val courseId: Long,

    @Column(name = "student_id")
    val studentId: Long,
) : Serializable
