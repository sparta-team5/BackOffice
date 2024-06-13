package domain.auth.tutor.controller

import domain.auth.dto.ChangePasswordRequest
import domain.auth.dto.LoginRequest
import domain.auth.dto.SignUpRequest
import domain.auth.tutor.service.TutorService
import domain.user.dto.TutorResponseDto
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/auth/tutor")
class TutorController(
    private val tutorService: TutorService
) {
    @PostMapping("/signup")
    fun signUpTutor(
        @RequestBody signUpRequest: SignUpRequest
    ): ResponseEntity<TutorResponseDto> {
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