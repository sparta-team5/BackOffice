package team5.backoffice.domain.auth.student.controller

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.*
import team5.backoffice.domain.auth.dto.ChangePasswordRequest
import team5.backoffice.domain.auth.dto.LoginRequest
import team5.backoffice.domain.auth.dto.SignUpRequest
import team5.backoffice.domain.auth.student.service.StudentService
import team5.backoffice.domain.user.dto.StudentResponse
import team5.backoffice.infra.security.UserPrincipal

@RestController
@RequestMapping("/auth/student")
class StudentAuthController(
    private val studentService: StudentService
) {

    @PostMapping("/signup")
    fun signUpStudent(
        @RequestBody signUpRequest: SignUpRequest
    ): ResponseEntity<StudentResponse> {
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(studentService.signUpStudent(signUpRequest))
    }

    @PostMapping("/login")
    fun loginStudent(
        @RequestBody loginRequest: LoginRequest
    ): ResponseEntity<String> {
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(studentService.loginStudent(loginRequest))
    }

    @PatchMapping("/change-password")
    @PreAuthorize("hasRole('ROLE_STUDENT')")
    fun changePassword(
        @RequestBody changePasswordRequest: ChangePasswordRequest,
        authentication: Authentication
    ): ResponseEntity<Boolean> {
        val studentPrincipal = authentication.principal as UserPrincipal
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(studentService.changeStudentPassword(changePasswordRequest, studentPrincipal.email))

    }

}