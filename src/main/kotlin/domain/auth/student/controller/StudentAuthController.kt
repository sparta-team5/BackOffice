package domain.auth.student.controller

import domain.auth.dto.ChangePasswordRequest
import domain.auth.dto.LoginRequest
import domain.auth.dto.SignUpRequest
import domain.auth.student.service.StudentService
import domain.user.dto.StudentResponse
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

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

    @PostMapping("/login/student")
    fun loginStudent(
        @RequestBody loginRequest: LoginRequest
    ): ResponseEntity<String> {
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(studentService.loginStudent(loginRequest))
    }

    @PatchMapping("/change-password")
    fun changePassword(
        @RequestBody changePasswordRequest: ChangePasswordRequest
    ): ResponseEntity<Boolean> {
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(studentService.changeStudentPassword(changePasswordRequest))

    }

}