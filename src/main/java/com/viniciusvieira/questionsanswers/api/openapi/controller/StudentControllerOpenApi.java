package com.viniciusvieira.questionsanswers.api.openapi.controller;

import com.viniciusvieira.questionsanswers.api.representation.models.ApplicationUserDto;
import com.viniciusvieira.questionsanswers.api.representation.models.StudentDto;
import com.viniciusvieira.questionsanswers.api.representation.requests.ApplicationUserRequestBody;
import com.viniciusvieira.questionsanswers.api.representation.requests.StudentRequestBody;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

import java.util.List;

@Tag(name = "Student")
public interface StudentControllerOpenApi {

    @Operation(summary = "Save Student", description = "Insert student in the database", responses = {
            @ApiResponse(responseCode = "201", description = "When Successful"),
            @ApiResponse(responseCode = "400", description = "When Have a Student Fields Empty", content = {
                    @Content(schema = @Schema(ref = "ExceptionDetails"))
            })
    })
    ResponseEntity<StudentDto> save(@RequestBody(description = "representation of a new student",
            required = true) StudentRequestBody studentRequestBody);

    @Operation(summary = "Save Student ApplicationUser", description = "Insert student applicationUser in the database",
            responses = {
                    @ApiResponse(responseCode = "201", description = "When Successful"),
                    @ApiResponse(responseCode = "400", description = "When Have a ApplicationUser Fields Empty", content = {
                            @Content(schema = @Schema(ref = "ExceptionDetails"))
                    }),
                    @ApiResponse(responseCode = "404", description = "When Student Not Found", content = {
                            @Content(schema = @Schema(ref = "ExceptionDetails"))
                    })
            })
    ResponseEntity<ApplicationUserDto> saveStudentApplicationUser(@Parameter(description = "id of a student", example = "1",
            required = true) Long idStudent, @RequestBody(description = "representation of a new applicationUser",
            required = true) ApplicationUserRequestBody applicationUserRequestBody);


    @Operation(summary = "Find student by name", description = "Return a list of students", responses = {
            @ApiResponse(responseCode = "200", description = "When Successful"),
            @ApiResponse(responseCode = "404", description = "When Student List is Empty", content = {
                    @Content(schema = @Schema(ref = "ExceptionDetails"))
            })
    })
    ResponseEntity<List<StudentDto>> findByName(@Parameter(description = "name of a student", example = "Estudante")
                                                String name);


    @Operation(summary = "Update Student", description = "Replace student in database", responses = {
            @ApiResponse(responseCode = "204", description = "When Successful"),
            @ApiResponse(responseCode = "404", description = "When Student Not Found By Id", content = {
                    @Content(schema = @Schema(ref = "ExceptionDetails"))
            }),
            @ApiResponse(responseCode = "400", description = "When RepresentatioN Student to Update Have Invalid Fields", content = {
                    @Content(schema = @Schema(ref = "ExceptionDetails"))
            })
    })
    ResponseEntity<StudentDto> replace(
            @Parameter(description = "id of a student", example = "1", required = true) Long idStudent,
            @RequestBody(description = "Representation of a student with updated data", required = true) StudentRequestBody studentRequestBody
    );

    @Operation(summary = "Delte Student", description = "Remove student in database", responses = {
            @ApiResponse(responseCode = "204", description = "When Successful"),
            @ApiResponse(responseCode = "404", description = "When Student Not Found By Id", content = {
                    @Content(schema = @Schema(ref = "ExceptionDetails"))
            })
    })
    ResponseEntity<Object> delete(@Parameter(description = "id of a student", example = "1", required = true)
                                  Long idStudent);

}
