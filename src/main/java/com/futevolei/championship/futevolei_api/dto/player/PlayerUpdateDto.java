package com.futevolei.championship.futevolei_api.dto.player;

import com.futevolei.championship.futevolei_api.model.Team;
import com.futevolei.championship.futevolei_api.model.enums.Registrations;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record PlayerUpdateDto(
        @NotBlank(message = "O nome do jogador é obrigatório.")
        @Size(min = 3, max = 100, message = "O nome do jogador deve ter entre 3 e 100 caracteres.")
        String name,
        Team team,
        Registrations registrations
) {
}
