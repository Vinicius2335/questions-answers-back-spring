package com.viniciusvieira.questionsanswers.api.openapi.controller;

import com.viniciusvieira.questionsanswers.api.representation.models.ApplicationUserDto;
import com.viniciusvieira.questionsanswers.api.representation.models.ProfessorDto;
import com.viniciusvieira.questionsanswers.api.representation.requests.ApplicationUserRequestBody;
import com.viniciusvieira.questionsanswers.api.representation.requests.ProfessorRequestBody;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

import java.util.List;

@Tag(name = "AdminProfessor")
public interface AdminProfessorControllerOpenApi {

    @Operation(summary = "Save Professor", description = "Insert professor in the database", responses = {
            @ApiResponse(responseCode = "201", description = "When Successful"),
            @ApiResponse(responseCode = "400", description = "When Have a Professor Fields Empty", content = {
                    @Content(schema = @Schema(ref = "ExceptionDetails"))
            })
    })
    ResponseEntity<ProfessorDto> save(@RequestBody(description = "representation of a new professor",
            required = true) ProfessorRequestBody professorRequestBody);

    @Operation(summary = "Save Professor ApplicationUser", description = "Insert professor applicationUser in the database",
            responses = {
                    @ApiResponse(responseCode = "201", description = "When Successful"),
                    @ApiResponse(responseCode = "400", description = "When Have a ApplicationUser Fields Empty", content = {
                            @Content(schema = @Schema(ref = "ExceptionDetails"))
                    }),
                    @ApiResponse(responseCode = "404", description = "When Professor Not Found", content = {
                            @Content(schema = @Schema(ref = "ExceptionDetails"))
                    })
            })
    ResponseEntity<ApplicationUserDto> saveProfessorApplicationUser(@Parameter(description = "id of a professor", example = "1",
            required = true) Long idProfessor, @RequestBody(description = "representation of a new applicationUser",
            required = true) ApplicationUserRequestBody applicationUserRequestBody);


    @Operation(summary = "Find professor by name", description = "Return a list of professors", responses = {
            @ApiResponse(responseCode = "200", description = "When Successful"),
            @ApiResponse(responseCode = "404", description = "When Professor List is Empty", content = {
                    @Content(schema = @Schema(ref = "ExceptionDetails"))
            })
    })
    ResponseEntity<List<ProfessorDto>> findByName(@Parameter(description = "name of a professor", example = "Professor")
                                                  String name);


    @Operation(summary = "Update Professor", description = "Replace professor in database", responses = {
            @ApiResponse(responseCode = "204", description = "When Successful"),
            @ApiResponse(responseCode = "404", description = "When Professor Not Found By Id", content = {
                    @Content(schema = @Schema(ref = "ExceptionDetails"))
            }),
            @ApiResponse(responseCode = "400", description = "When Representation Professor to Update Have Invalid Fields",
                    content = {
                            @Content(schema = @Schema(ref = "ExceptionDetails"))
                    })
    })
    ResponseEntity<ProfessorDto> replace(
            @Parameter(description = "id of a professor", example = "1", required = true) Long idProfessor,
            @RequestBody(description = "Representation of a professor with updated data", required = true)
            ProfessorRequestBody professorRequestBody
    );

    @Operation(summary = "Delete Professor", description = "Remove professor in database", responses = {
            @ApiResponse(responseCode = "204", description = "When Successful"),
            @ApiResponse(responseCode = "404", description = "When Professor Not Found By Id", content = {
                    @Content(schema = @Schema(ref = "ExceptionDetails"))
            })
    })
    ResponseEntity<Object> delete(@Parameter(description = "id of a professor", example = "1", required = true)
                                  Long idProfessor);

}
