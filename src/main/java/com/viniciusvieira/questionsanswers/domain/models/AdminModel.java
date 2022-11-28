package com.viniciusvieira.questionsanswers.domain.models;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.persistence.*;
import java.io.Serial;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@Entity
@Table(name = "TB_ADMIN")
public class AdminModel implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "The id of the admin")
    private Long idAdmin;

    @Schema(description = "The name of the admin")
    @Column(nullable = false)
    private String name;

    @Schema(description = "The email of the admin")
    @Column(nullable = false, unique = true)
    private String email;

    @Schema(description = "describes whether the choice is enabled or not")
    @Column(nullable = false, columnDefinition = "boolean default true")
    private boolean enabled;
}
