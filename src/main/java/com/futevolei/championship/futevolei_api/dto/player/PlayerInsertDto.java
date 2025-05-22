package com.futevolei.championship.futevolei_api.dto.player;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record PlayerInsertDto(
        @NotBlank(message = "O nome do jogador é obrigatório.")
        @Size(min = 3, max = 100, message = "O nome do jogador deve ter entre 3 e 100 caracteres.")
        String name

) {
}
