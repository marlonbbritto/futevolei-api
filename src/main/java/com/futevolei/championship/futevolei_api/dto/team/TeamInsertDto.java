package com.futevolei.championship.futevolei_api.dto.team;

import com.futevolei.championship.futevolei_api.dto.championship.ChampionshipDto;
import com.futevolei.championship.futevolei_api.dto.player.PlayerSummaryDto;
import com.futevolei.championship.futevolei_api.model.Championship;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;

public record TeamInsertDto(
        @NotBlank(message = "O nome do time é obrigatório.")
        @Size(min = 3, max = 100, message = "O nome do time deve ter entre 3 e 100 caracteres.")
        String name,
        @NotNull
        Long championshipId
) {
}
