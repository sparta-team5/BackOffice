package team5.backoffice.domain.user.controller

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.*
import team5.backoffice.domain.user.dto.StudentResponse
import team5.backoffice.domain.user.dto.UpdateStudentRequest
import team5.backoffice.domain.user.service.UserService
import team5.backoffice.infra.security.UserPrincipal

@RestController
@RequestMapping("/students/{studentId}")
class StudentController(
    private val userService: UserService,
) {
    @GetMapping
    fun getStudent(@PathVariable studentId: Long): ResponseEntity<StudentResponse> {
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(userService.getStudentById(studentId))
    }

    @PutMapping
    @PreAuthorize("hasRole('ROLE_STUDENT')")
    fun updateStudent(
        @PathVariable studentId: Long,
        @RequestBody updateStudentRequest: UpdateStudentRequest,
        authentication: Authentication
    ): ResponseEntity<StudentResponse> {
        val student = authentication.principal as UserPrincipal
        if (studentId == student.id) {
            return ResponseEntity
                .status(HttpStatus.OK)
                .body(userService.updateStudentById(student.id, updateStudentRequest))
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build()
    }

    @DeleteMapping
    @PreAuthorize("hasRole('ROLE_STUDENT')")
    fun deleteStudent(@PathVariable studentId: Long, authentication: Authentication): ResponseEntity<Unit> {
        val student = authentication.principal as UserPrincipal
        if (studentId == student.id) {
            return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .body(userService.deleteStudentById(student.id))
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build()
    }
}