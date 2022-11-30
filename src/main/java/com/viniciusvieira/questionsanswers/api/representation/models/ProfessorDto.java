package com.viniciusvieira.questionsanswers.api.representation.models;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProfessorDto {
    @Schema(description = "The id of the professor")
    private Long idProfessor;

    @Schema(description = "The name of the professor")
    private String name;

    @Schema(description = "The email of the professor")
    private String email;
}
