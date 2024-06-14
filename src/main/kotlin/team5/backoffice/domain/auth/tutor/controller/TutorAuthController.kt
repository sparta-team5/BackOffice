package team5.backoffice.domain.auth.tutor.controller

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.*
import team5.backoffice.domain.auth.dto.ChangePasswordRequest
import team5.backoffice.domain.auth.dto.LoginRequest
import team5.backoffice.domain.auth.dto.SignUpRequest
import team5.backoffice.domain.auth.tutor.service.TutorService
import team5.backoffice.domain.user.dto.TutorResponse
import team5.backoffice.infra.security.UserPrincipal

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
    @PreAuthorize("hasRole('ROLE_TUTOR')")
    fun changePassword(
        @RequestBody changePasswordRequest: ChangePasswordRequest,
        authentication: Authentication
    ): ResponseEntity<Boolean> {
        val tutorPrincipal = authentication.principal as UserPrincipal
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(tutorService.changeTutorPassword(changePasswordRequest, tutorPrincipal.email))

    }
}