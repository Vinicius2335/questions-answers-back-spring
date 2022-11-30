package com.viniciusvieira.questionsanswers.api.controllers;

import com.viniciusvieira.questionsanswers.api.openapi.controller.AdminStudentControllerOpenApi;
import com.viniciusvieira.questionsanswers.api.representation.models.ApplicationUserDto;
import com.viniciusvieira.questionsanswers.api.representation.models.StudentDto;
import com.viniciusvieira.questionsanswers.api.representation.requests.ApplicationUserRequestBody;
import com.viniciusvieira.questionsanswers.api.representation.requests.StudentRequestBody;
import com.viniciusvieira.questionsanswers.domain.services.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/admin/student")
@RequiredArgsConstructor
public class AdminStudentController implements AdminStudentControllerOpenApi {
    private final StudentService studentService;

    @Override
    @PostMapping
    public ResponseEntity<StudentDto> save(@Valid @RequestBody StudentRequestBody studentRequestBody) {
        return ResponseEntity.status(HttpStatus.CREATED).body(studentService.saveStudent(studentRequestBody));
    }

    @Override
    @PostMapping("/{idStudent}")
    public ResponseEntity<ApplicationUserDto> saveStudentApplicationUser(@PathVariable Long idStudent
            , @Valid @RequestBody ApplicationUserRequestBody applicationUserRequestBody) {
        return ResponseEntity.status(HttpStatus.CREATED).body(studentService.saveApplicationUserStudent(idStudent,
                applicationUserRequestBody));
    }


    @Override
    @GetMapping("/students")
    public ResponseEntity<List<StudentDto>> findByName(@RequestParam(required = false, defaultValue = "") String name) {
        return ResponseEntity.status(HttpStatus.OK).body(studentService.findByName(name));
    }

    @Override
    @PutMapping("/{idStudent}")
    public ResponseEntity<StudentDto> replace(@PathVariable Long idStudent, @Valid @RequestBody StudentRequestBody
            studentRequestBody) {
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(studentService.replace(idStudent, studentRequestBody));
    }

    @Override
    @DeleteMapping("/{idStudent}")
    public ResponseEntity<Object> delete(@PathVariable Long idStudent) {
        studentService.delete(idStudent);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Student deleted successfully");
    }
}

//TEST