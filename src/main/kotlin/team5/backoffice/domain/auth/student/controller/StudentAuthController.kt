package team5.backoffice.domain.auth.student.controller

import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.*
import team5.backoffice.domain.auth.dto.*
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
        @RequestBody @Valid signUpRequest: SignUpRequest
    ): ResponseEntity<StudentResponse> {
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(studentService.signUpStudent(signUpRequest))
    }

    @PostMapping("/login")
    fun loginStudent(
        @RequestBody loginRequest: LoginRequest
    ): ResponseEntity<TokenResponse> {
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(studentService.loginStudent(loginRequest))
    }

    @PostMapping("/token")
    fun regenerateToken(
        @RequestBody tokenRequest: TokenRequest
    ): ResponseEntity<TokenResponse> {
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(studentService.regenerateToken(tokenRequest))
    }

    @PatchMapping("/change-password")
    @PreAuthorize("hasRole('ROLE_STUDENT')")
    fun changePassword(
        @Valid @RequestBody changePasswordRequest: ChangePasswordRequest,
        authentication: Authentication
    ): ResponseEntity<String> {
        val studentPrincipal = authentication.principal as UserPrincipal
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(studentService.changeStudentPassword(changePasswordRequest, studentPrincipal.id))

    }

}