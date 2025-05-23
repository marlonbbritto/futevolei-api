package com.futevolei.championship.futevolei_api.dto.player;

import com.futevolei.championship.futevolei_api.model.Team;
import com.futevolei.championship.futevolei_api.model.enums.Registrations;

public record PlayerDto(
        Long id,
        String name,
        Team team,
        Registrations registrations
) {
}
