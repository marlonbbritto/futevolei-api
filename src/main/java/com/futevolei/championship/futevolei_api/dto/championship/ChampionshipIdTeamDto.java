package com.futevolei.championship.futevolei_api.dto.championship;

import jakarta.validation.constraints.NotNull;

public record ChampionshipIdTeamDto(
        @NotNull
        Long id
) {
}
