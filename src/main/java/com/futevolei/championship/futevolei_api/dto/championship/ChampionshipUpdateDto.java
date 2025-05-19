package com.futevolei.championship.futevolei_api.dto.championship;

import java.time.LocalDate;

public record ChampionshipUpdateDto(
        String name,
        LocalDate startDate,
        String city,
        Integer numberOfTeams
) {
}
