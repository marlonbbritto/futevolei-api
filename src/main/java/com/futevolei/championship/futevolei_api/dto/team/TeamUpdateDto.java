package com.futevolei.championship.futevolei_api.dto.team;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record TeamUpdateDto(

        @Size(min = 3, max = 100, message = "O nome do time deve ter entre 3 e 100 caracteres.")
        String name,
        Long championshipId
) {
}
