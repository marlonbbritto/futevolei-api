package com.futevolei.championship.futevolei_api.dto.team;

import jakarta.validation.constraints.NotNull;

public record TeamAddPlayerIdDto(
        @NotNull
        Long id
) {
}
