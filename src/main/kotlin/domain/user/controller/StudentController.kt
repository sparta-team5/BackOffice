package domain.user.controller

import domain.user.dto.StudentResponseDto
import domain.user.dto.UpdateStudentRequestDto
import domain.user.service.UserService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/students/{student_id}")
class StudentController (
    private val userService : UserService,
        ){

    @GetMapping()
    fun getStudent(@PathVariable student_id :Long): ResponseEntity<StudentResponseDto> {
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(userService.getStudentById(student_id))
    }

    @PutMapping()
    fun updateStudent(
        @PathVariable student_id :Long,
        @RequestBody updateStudentRequest : UpdateStudentRequestDto
    ): ResponseEntity<StudentResponseDto> {

        return try {
            ResponseEntity
                .status(HttpStatus.OK)
                .body(userService.updateStudentById(student_id, updateStudentRequest))
        } catch (e: IllegalAccessException) {
            ResponseEntity.status(403).build()
        }
    }

    @DeleteMapping()
    fun deleteStudent(@PathVariable student_id: Long): ResponseEntity<Unit> {

        return try {
            ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .body(userService.deleteStudentById(student_id))
        } catch (e: IllegalAccessException) {
            ResponseEntity
                .status(403).build()
        }
    }
}