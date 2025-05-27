package com.futevolei.championship.futevolei_api.dto.player;

import com.futevolei.championship.futevolei_api.model.Team;
import com.futevolei.championship.futevolei_api.model.enums.Registrations;

public record PlayerSummaryDto(
        Long id,
        String name,
        Registrations registrations
) {
}
