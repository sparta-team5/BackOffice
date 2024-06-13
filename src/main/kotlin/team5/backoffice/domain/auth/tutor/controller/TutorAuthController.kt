package team5.backoffice.domain.auth.tutor.controller

import team5.backoffice.domain.auth.dto.ChangePasswordRequest
import team5.backoffice.domain.auth.dto.LoginRequest
import team5.backoffice.domain.auth.dto.SignUpRequest
import team5.backoffice.domain.auth.tutor.service.TutorService
import team5.backoffice.domain.user.dto.TutorResponse
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/auth/tutor")
class TutorAuthController(
    private val tutorService: TutorService
) {
    @PostMapping("/signup")
    fun signUpTutor(
        @RequestBody signUpRequest: SignUpRequest
    ): ResponseEntity<TutorResponse> {
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(tutorService.signUpTutor(signUpRequest))
    }

    @PostMapping("/login")
    fun loginTutor(
        @RequestBody loginRequest: LoginRequest
    ): ResponseEntity<String> {
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(tutorService.loginTutor(loginRequest))
    }

    @PatchMapping("/change-password")
    fun changePassword(
        @RequestBody changePasswordRequest: ChangePasswordRequest
    ): ResponseEntity<Boolean> {
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(tutorService.changeTutorPassword(changePasswordRequest))

    }
}