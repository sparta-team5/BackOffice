package domain.user.controller

import domain.user.dto.StudentResponseDto
import domain.user.dto.UpdateStudentRequestDto
import domain.user.service.UserService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/students/{studentId}")
class StudentController (
    private val userService : UserService,
        ){

    @GetMapping()
    fun getStudent(@PathVariable studentId :Long): ResponseEntity<StudentResponseDto> {
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(userService.getStudentById(studentId))
    }

    @PutMapping()
    fun updateStudent(
        @PathVariable studentId :Long,
        @RequestBody updateStudentRequest : UpdateStudentRequestDto
    ): ResponseEntity<StudentResponseDto> {

        return try {
            ResponseEntity
                .status(HttpStatus.OK)
                .body(userService.updateStudentById(studentId, updateStudentRequest))
        } catch (e: IllegalAccessException) {
            ResponseEntity.status(403).build()
        }
    }

    @DeleteMapping()
    fun deleteStudent(@PathVariable studentId: Long): ResponseEntity<Unit> {

        return try {
            ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .body(userService.deleteStudentById(studentId))
        } catch (e: IllegalAccessException) {
            ResponseEntity
                .status(403).build()
        }
    }
}