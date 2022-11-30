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
public class ApplicationUserDto {
    @Schema(description = "The id of the user")
    private Long idApplicationUser;

    @Schema(description = "The username of the user, needed to access the features")
    private String username;

    @Schema(description = "The password of the user, need to access the features")
    private String password;
}
