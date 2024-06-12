package domain.auth.student.controller

import domain.auth.dto.ChangePasswordRequest
import domain.auth.dto.LoginRequest
import domain.auth.dto.SignUpRequest
import domain.auth.student.service.StudentService
import domain.user.dto.StudentResponseDto
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/auth/student")
class StudentController(
    private val studentService: StudentService
) {

    @GetMapping("/signup")
    fun signUpStudent(
        @RequestBody signUpRequest: SignUpRequest
    ): ResponseEntity<StudentResponseDto> {
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(studentService.signUpStudent(signUpRequest))
    }

    @GetMapping("/login/student")
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
            .status(HttpStatus.CREATED)
            .body(studentService.changeStudentPassword(changePasswordRequest))

    }

}